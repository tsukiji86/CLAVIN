package com.bericotech.clavin.index;

import static com.bericotech.clavin.index.IndexField.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.gazetteer.FeatureCode;
import com.bericotech.clavin.gazetteer.GeoName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*#####################################################################
 *
 * CLAVIN (Cartographic Location And Vicinity INdexer)
 * ---------------------------------------------------
 *
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 *
 * ====================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * ====================================================================
 *
 * IndexDirectoryBuilder.java
 *
 *###################################################################*/

/**
 * Builds a Lucene index of geographic entries based on
 * the GeoNames gazetteer.
 *
 * This program is run one-time before CLAVIN can be used.
 *
 */
public class IndexDirectoryBuilder {
    private final static Logger LOG = LoggerFactory.getLogger(IndexDirectoryBuilder.class);
    private static final String HELP_OPTION = "help";
    private static final String FULL_ANCESTRY_OPTION = "with-full-ancestry";
    private static final String GAZETTEER_FILES_OPTION = "gazetteer-files";
    private static final String INDEX_PATH_OPTION = "index-path";
    private static final String REPLACE_INDEX_OPTION = "replace-index";

    private static final String[] DEFAULT_GAZETTEER_FILES = new String[] {
        "./allCountries.txt",
        "./src/main/resources/SupplementaryGazetteer.txt"
    };
    private static final String DEFAULT_INDEX_DIRECTORY = "./IndexDirectory";

    // the GeoNames gazetteer file to be loaded
    static String pathToGazetteer = "./allCountries.txt";

    private final Map<String, GeoName> adminMap;
    private final Map<String, Set<GeoName>> unresolvedMap;
    private final boolean fullAncestry;

    private IndexWriter indexWriter;
    private int indexCount;

    private IndexDirectoryBuilder(final boolean fullAncestryIn) {
        adminMap = new TreeMap<String, GeoName>();
        unresolvedMap = new TreeMap<String, Set<GeoName>>();
        this.fullAncestry = fullAncestryIn;
    }

    public void buildIndex(final File indexDir, final List<File> gazetteerFiles) throws IOException {
        LOG.info("Indexing... please wait.");

        indexCount = 0;

        // Create a new index file on disk, allowing Lucene to choose
        // the best FSDirectory implementation given the environment.
        FSDirectory index = FSDirectory.open(indexDir);

        // indexing by lower-casing & tokenizing on whitespace
        Analyzer indexAnalyzer = new WhitespaceLowerCaseAnalyzer();

        // create the object that will actually build the Lucene index
        indexWriter = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_47, indexAnalyzer));

        // let's see how long this takes...
        Date start = new Date();

        // load GeoNames gazetteer into Lucene index
        String line;
        int count = 0;
        for (File gazetteer : gazetteerFiles) {
            LOG.info("Processing Gazetteer: {}", gazetteer.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(gazetteer), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                try {
                    count += 1;
                    // print progress update to console
                    if (count % 100000 == 0 ) {
                        LOG.info("rowcount: " + count);
                    }
                    GeoName geoName = GeoName.parseFromGeoNamesRecord(line);
                    resolveAncestry(geoName);
                } catch (IOException e) {
                    LOG.info("Skipping... Error on line: {}", line);
                } catch (RuntimeException re) {
                    LOG.info("Skipping... Error on line: {}", line);
                }
            }
            reader.close();
        }

        // that wasn't so long, was it?
        Date stop = new Date();

        LOG.info("Unresolved GeoNames (Pre-resolution)");
        logUnresolved();

        resolveUnresolved();

        LOG.info("Unresolved GeoNames (Post-resolution)");
        logUnresolved();

        LOG.info("Indexing unresolved GeoNames.");
        for (Set<GeoName> geos : unresolvedMap.values()) {
            for (GeoName nm : geos) {
                indexGeoName(nm);
            }
        }

        LOG.info("[DONE]");
        LOG.info("{} geonames added to index. ({} records)", indexWriter.maxDoc(), indexCount);
        LOG.info("Merging indices... please wait.");

        indexWriter.close();
        index.close();

        LOG.info("[DONE]");

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        long elapsed_MILLIS = stop.getTime() - start.getTime();
        LOG.info("Process started: " + df.format(start) + ", ended: " + df.format(stop)
                + "; elapsed time: " + MILLISECONDS.toSeconds(elapsed_MILLIS) + " seconds.");
    }

    private void resolveAncestry(final GeoName geoname) throws IOException {
        // set this GeoName's parent if it is known
        String parentKey = geoname.getParentAncestryKey();
        if (parentKey != null) {
            // if we cannot successfully set the parent, add to the unresolved map,
            // waiting for a parent to be set
            if (!geoname.setParent(adminMap.get(parentKey)) || !geoname.isAncestryResolved()) {
                Set<GeoName> unresolved = unresolvedMap.get(parentKey);
                if (unresolved == null) {
                    unresolved = new HashSet<GeoName>();
                    unresolvedMap.put(parentKey, unresolved);
                }
                unresolved.add(geoname);
            }
        }
        // if this geoname is fully resolved, add it to the index
        if (geoname.isAncestryResolved()) {
            indexGeoName(geoname);
        }

        // if this is an administrative division, configure the parent of any waiting
        // GeoNames and notify all 2nd level and further descendants their tree has been
        // updated
        String myKey = geoname.getAncestryKey();
        if (myKey != null) {
            GeoName conflict = adminMap.get(myKey);
            if (conflict != null) {
                LOG.error(String.format("Resolved duplicate admin key [%s] for GeoNames (%d %s:%s %s) and (%d %s:%s %s)",
                        myKey, conflict.getGeonameID(), conflict.getFeatureClass(), conflict.getFeatureCode(), conflict.getName(),
                        geoname.getGeonameID(), geoname.getFeatureClass(), geoname.getFeatureCode(), geoname.getName()));
            }
            adminMap.put(myKey, geoname);
            checkDescendantsResolved(geoname, true);
        }
    }

    private void checkDescendantsResolved(final GeoName geoname, final boolean setParent) throws IOException {
        String key = geoname.getAncestryKey();
        if (key != null) {
            Set<GeoName> descendants = unresolvedMap.get(key);
            if (descendants != null) {
                // use an iterator so we can remove elements
                Iterator<GeoName> iter = descendants.iterator();
                while (iter.hasNext()) {
                    GeoName desc = iter.next();
                    if (setParent) {
                        if (!desc.setParent(geoname)) {
                            LOG.error("Error setting parent [{}] of GeoName [{}].", geoname, desc);
                        }
                    }
                    if (desc.isAncestryResolved()) {
                        checkDescendantsResolved(desc, false);
                        indexGeoName(desc);
                        iter.remove();
                    }
                }
                if (descendants.isEmpty()) {
                    unresolvedMap.remove(key);
                }
            }
        }
    }

    private void resolveUnresolved() throws IOException {
        // sort keys in ascending order by level of specificity and name
        Set<String> keys = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(final String strA, final String strB) {
                int specA = strA.split("\\.").length;
                int specB = strB.split("\\.").length;
                return specA != specB ? specA - specB : strA.compareTo(strB);
            }
        });
        keys.addAll(unresolvedMap.keySet());

        // iterate over keys, attempting to resolve less specific keys first; if
        // they are resolved, this may result in more specific keys being resolved
        // as well
        for (String key : keys) {
            String subKey = key;
            GeoName parent = null;
            int lastDot;
            while (parent == null && (lastDot = subKey.lastIndexOf(".")) > 0) {
                subKey = key.substring(0, lastDot);
                parent = adminMap.get(subKey);
            }
            if (parent != null) {
                Set<GeoName> unresolved = unresolvedMap.get(key);
                if (unresolved == null) {
                    // resolving a higher-level key also resolved this key; do nothing
                    break;
                }
                Iterator<GeoName> iter = unresolved.iterator();
                // use iterator so we can remove
                while (iter.hasNext()) {
                    GeoName geoName = iter.next();
                    // first check to see if a previous loop resolved all parents
                    if (geoName.isAncestryResolved()) {
                        indexGeoName(geoName);
                        iter.remove();
                    } else if (geoName.setParent(parent)) {
                        if (geoName.isAncestryResolved()) {
                            // ancestry has been resolved, remove from the unresolved collection
                            indexGeoName(geoName);
                            iter.remove();
                        } else {
                            LOG.error("GeoName [{}] should be fully resolved. (parent: {})", geoName, parent);
                        }
                    } else {
                        LOG.error("Unable to set parent of {} to {}", geoName, parent);
                    }
                }
                if (unresolved.isEmpty()) {
                    unresolvedMap.remove(key);
                }
            } else {
                LOG.error("Unable to resolve parent for GeoName key: {}", key);
            }
        }
    }

    /**
     * Builds a set of Lucene documents for the provided GeoName, indexing
     * each using all available names and storing the entire ancestry path
     * for each GeoName in the index.  See {@link IndexField} for descriptions
     * of the fields indexed for each document.
     *
     * @param geoName the GeoName to index
     * @return the documents to be added to the index
     * @throws IOException if an error occurs while indexing
     */
    private void indexGeoName(final GeoName geoName) throws IOException {
        indexCount++;
        // find all unique names for this GeoName
        String nm = geoName.getName();
        String asciiNm = geoName.getAsciiName();
        Set<String> names = new HashSet<String>();
        names.add(nm);
        names.add(asciiNm);
        names.addAll(geoName.getAlternateNames());
        // if this is a top-level administrative division, add its primary and alternate country codes
        // if they are not already found in the name or alternate names
        if (geoName.isTopLevelAdminDivision()) {
            if (geoName.getPrimaryCountryCode() != null) {
                names.add(geoName.getPrimaryCountryCode().name());
            }
            for (CountryCode cc : geoName.getAlternateCountryCodes()) {
                names.add(cc.name());
            }
        }
        names.remove(null);
        names.remove("");

        // reuse a single Document and field instances
        Document doc = new Document();
        doc.add(new StoredField(GEONAME.key(), fullAncestry ? geoName.getGazetteerRecordWithAncestry() : geoName.getGazetteerRecord()));
        doc.add(new IntField(GEONAME_ID.key(), geoName.getGeonameID(), Field.Store.YES));
        // index the direct parent ID in the PARENT_ID field
        GeoName parent = geoName.getParent();
        if (parent != null) {
            doc.add(new IntField(PARENT_ID.key(), parent.getGeonameID(), Field.Store.YES));
        }
        // index all ancestor IDs in the ANCESTOR_IDS field; this is a secondary field
        // so it can be used to restrict searches and PARENT_ID can be used for ancestor
        // resolution
        while (parent != null) {
            doc.add(new IntField(ANCESTOR_IDS.key(), parent.getGeonameID(), Field.Store.YES));
            parent = parent.getParent();
        }
        doc.add(new LongField(POPULATION.key(), geoName.getPopulation(), Field.Store.YES));
        doc.add(new IntField(HISTORICAL.key(), IndexField.getBooleanIndexValue(geoName.getFeatureCode().isHistorical()), Field.Store.NO));
        doc.add(new StringField(FEATURE_CODE.key(), geoName.getFeatureCode().name(), Field.Store.NO));

        // create a unique Document for each name of this GeoName
        TextField nameField = new TextField(INDEX_NAME.key(), "", Field.Store.YES);
        doc.add(nameField);
        for (String name : names) {
            nameField.setStringValue(name);
            indexWriter.addDocument(doc);
        }
    }

    private void logUnresolved() {
        int unresolvedGeoCount = 0;
        Map<String, Integer> unresolvedCodeMap = new TreeMap<String, Integer>();
        Map<String, Integer> missingCodeMap = new TreeMap<String, Integer>();
        for (Map.Entry<String, Set<GeoName>> entry : unresolvedMap.entrySet()) {
            LOG.trace("{}: {} unresolved GeoNames", entry.getKey(), entry.getValue().size());
            unresolvedGeoCount += entry.getValue().size();
            FeatureCode code;
            switch (entry.getKey().split("\\.").length) {
                case 1:
                    code = FeatureCode.PCL;
                    break;
                case 2:
                    code = FeatureCode.ADM1;
                    break;
                case 3:
                    code = FeatureCode.ADM2;
                    break;
                case 4:
                    code = FeatureCode.ADM3;
                    break;
                case 5:
                    code = FeatureCode.ADM4;
                    break;
                default:
                    LOG.error("Unexpected ancestry key: {}", entry.getKey());
                    code = FeatureCode.NULL;
                    break;
            }
            if (missingCodeMap.containsKey(code.name())) {
                missingCodeMap.put(code.name(), missingCodeMap.get(code.name())+1);
            } else {
                missingCodeMap.put(code.name(), 1);
            }

            for (GeoName geo : entry.getValue()) {
                String featKey = String.format("%s:%s", geo.getFeatureClass(), geo.getFeatureCode());
                if (unresolvedCodeMap.containsKey(featKey)) {
                    unresolvedCodeMap.put(featKey, unresolvedCodeMap.get(featKey)+1);
                } else {
                    unresolvedCodeMap.put(featKey, 1);
                }
            }
        }
        LOG.info("Found {} administrative divisions.", adminMap.size());
        LOG.info("Found {} missing administrative keys.", unresolvedMap.size());
        for (String code : missingCodeMap.keySet()) {
            LOG.info("{}: {}", code, missingCodeMap.get(code));
        }
        LOG.info("{} total unresolved GeoNames", unresolvedGeoCount);
        for (String key : unresolvedCodeMap.keySet()) {
            LOG.trace("{}: {}", key, unresolvedCodeMap.get(key));
        }
    }

    /**
     * Turns a GeoNames gazetteer file into a Lucene index, and adds
     * some supplementary gazetteer records at the end.
     *
     * @param args              not used
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Options options = getOptions();
        CommandLine cmd = null;
        CommandLineParser parser = new GnuParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            LOG.error(pe.getMessage());
            printHelp(options);
            System.exit(-1);
        }

        if (cmd.hasOption(HELP_OPTION)) {
            printHelp(options);
            System.exit(0);
        }

        String indexPath = cmd.getOptionValue(INDEX_PATH_OPTION, DEFAULT_INDEX_DIRECTORY);
        String[] gazetteerPaths = cmd.getOptionValues(GAZETTEER_FILES_OPTION);
        if (gazetteerPaths == null || gazetteerPaths.length == 0) {
            gazetteerPaths = DEFAULT_GAZETTEER_FILES;
        }
        boolean replaceIndex = cmd.hasOption(REPLACE_INDEX_OPTION);
        boolean fullAncestry = cmd.hasOption(FULL_ANCESTRY_OPTION);

        File idir = new File(indexPath);
        // if the index directory exists, delete it if we are replacing, otherwise
        // exit gracefully
        if (idir.exists() ) {
            if (replaceIndex) {
                LOG.info("Replacing index: {}", idir.getAbsolutePath());
                FileUtils.deleteDirectory(idir);
            } else {
                LOG.info("{} exists. Remove the directory and try again.", idir.getAbsolutePath());
                System.exit(-1);
            }
        }

        List<File> gazetteerFiles = new ArrayList<File>();
        for (String gp : gazetteerPaths) {
            File gf = new File(gp);
            if (gf.isFile() && gf.canRead()) {
                gazetteerFiles.add(gf);
            } else {
                LOG.info("Unable to read Gazetteer file: {}", gf.getAbsolutePath());
            }
        }
        if (gazetteerFiles.isEmpty()) {
            LOG.error("No Gazetteer files found.");
            System.exit(-1);
        }
        new IndexDirectoryBuilder(fullAncestry).buildIndex(idir, gazetteerFiles);
    }

    private static Options getOptions() {
        Options options = new Options();

        options.addOption(OptionBuilder
                .withLongOpt(HELP_OPTION)
                .withDescription("Print help")
                .create('?'));

        options.addOption(OptionBuilder
                .withLongOpt(FULL_ANCESTRY_OPTION)
                .withDescription("Store the gazetteer records for the full ancestry tree of each element."
                        + " This will increase performance at the expense of a larger index.")
                .create());

        options.addOption(OptionBuilder
                .withLongOpt(GAZETTEER_FILES_OPTION)
                .withDescription(String.format("The ':'-separated list of input Gazetteer files to parse.  Default: %s",
                        StringUtils.join(DEFAULT_GAZETTEER_FILES, ':')))
                .hasArgs()
                .withValueSeparator(':')
                .create('i'));

        options.addOption(OptionBuilder
                .withLongOpt(INDEX_PATH_OPTION)
                .withDescription(String.format("The path to the output index directory. Default: %s", DEFAULT_INDEX_DIRECTORY))
                .hasArg()
                .create('o'));

        options.addOption(OptionBuilder
                .withLongOpt(REPLACE_INDEX_OPTION)
                .withDescription("Replace an existing index if it exists. If this option is not specified,"
                        + "index processing will fail if an index already exists at the specified location.")
                .create('r'));

        return options;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("run", options, true);
    }
}
