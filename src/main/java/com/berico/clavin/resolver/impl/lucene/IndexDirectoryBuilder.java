package com.berico.clavin.resolver.impl.lucene;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.util.GeonamesUtils;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Shape;

public class IndexDirectoryBuilder {

	public final static Logger logger = LoggerFactory.getLogger(IndexDirectoryBuilder.class);
	
	// the GeoNames gazetteer file to be loaded
	String pathToGazetteer = "./allCountries.txt";
	String indexDirectory = "./IndexDirectory";
	String supplemental = "./src/main/resources/SupplementaryGazetteer.txt";
	
	// Lucene Spatial Context
	SpatialContext spatialContext;
	
	// Spatial Indexing Strategy
	SpatialStrategy spatialStrategy;

	// Lucene Fields we will reuse when building the index.
	TextField indexNameField = new TextField(FieldConstants.NAME, "", Field.Store.YES);
  	StoredField geonameField = new StoredField(FieldConstants.GEONAME, "");
  	IntField geonameIdField = new IntField(FieldConstants.GEONAME_ID, -1, Field.Store.NO);
  	NumericDocValuesField populationField = new NumericDocValuesField(FieldConstants.POPULATION, -1l);
  	StoredField geospatialField = new StoredField(FieldConstants.GEOMETRY, "");
	
	
	public IndexDirectoryBuilder() throws IOException {
		
		logger.info("Indexing... please wait.");
		
		// Create a new index file on disk, allowing Lucene to choose
		// the best FSDirectory implementation given the environment.
		// TODO: delete this directory first, if it exists
		FSDirectory index = FSDirectory.open(new File(indexDirectory));
		
		// indexing by lower-casing & tokenizing on whitespace
		Analyzer indexAnalyzer = new WhitespaceLowerCaseAnalyzer();
		
		// create the object that will actually build the Lucene index
		IndexWriter indexWriter = new IndexWriter(index, 
				new IndexWriterConfig(Version.LUCENE_43, indexAnalyzer));
		
		spatialContext = SpatialContext.GEO;
		
		spatialStrategy = 
				new RecursivePrefixTreeStrategy(
						new GeohashPrefixTree(spatialContext, 11), 
						FieldConstants.GEOMETRY);
		
		// open the gazetteer files to be loaded
		BufferedReader r = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(
								new File(pathToGazetteer)), "UTF-8"));
		
		BufferedReader r2 = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(
								new File(supplemental )), "UTF-8"));
		
		String line;
		
		// let's see how long this takes...
		Date start = new Date();
		
		logger.debug("Writing GeoNames Gazatteer to Index.");
		
		// load GeoNames gazetteer into Lucene index
		while ((line = r.readLine()) != null){
			addToIndex(indexWriter, line);
			reportProgess();
		}
		
		logger.debug("Writing Supplemental Gazatteer to Index.");
		
		// add supplementary gazetteer records to index
		while ((line = r2.readLine()) != null){
			addToIndex(indexWriter, line);
			reportProgess();
		}
			
		
		// that wasn't so long, was it?
		Date stop = new Date();
		
		logger.info("[DONE]");
		logger.info(indexWriter.maxDoc() + " geonames added to index.");
		logger.info("Merging indices... please wait.");
		
		indexWriter.close();
		index.close();
		r.close();
		r2.close();
		
		logger.info("[DONE]");
		
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		long elapsed_MILLIS = stop.getTime() - start.getTime();
		logger.info("Process started: " + df.format(start) + ", ended: " + df.format(stop)
				+ "; elapsed time: " + MILLISECONDS.toSeconds(elapsed_MILLIS) + " seconds.");
	}
	
	long progress = 0;
	
	public void reportProgess(){
		
		progress++;
		
		if ((progress % 1000) == 0){
			
			logger.debug("1000 records indexed.");
		}
	}
	
	/**
	 * Turns a GeoNames gazetteer file into a Lucene index, and adds
	 * some supplementary gazetteer records at the end.
	 * 
	 * @param args				not used
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		new IndexDirectoryBuilder();
	}
	
	/**
	 * Adds entries to the Lucene index for each unique name associated
	 * with a {@link Place} object.
	 * 
	 * @param indexWriter	the object that actually builds the Lucene index
	 * @param geonameEntry	single record from GeoNames gazetteer
	 * @throws IOException
	 */
  	private void addToIndex(IndexWriter indexWriter, String geonameEntry) throws IOException {
  		
  		// create a GeoName object from a single gazetteer record
  		Place geoname = GeonamesUtils.parseFromGeoNamesRecord(geonameEntry);
  		
  		indexWriter.addDocument(buildDoc(geonameEntry, geoname));
  	}
  	
  	/**
  	 * Builds a Lucene document to be added to the index based on a
  	 * specified name for the location and the corresponding
  	 * {@link Place} object.
  	 * 
  	 * @param name			name to serve as index key
  	 * @param geonameEntry	string from GeoNames gazetteer
  	 * @param place		GeoName Entry
  	 * @return
  	 */
  	private Document buildDoc(String geonameEntry, Place place) {
  		
  		// in case you're wondering, yes, this is a non-standard use of
  		// the Lucene Document construct
	    Document doc = new Document();
	    
	    // this is essentially the key we'll try to match location
	    // names against
	    addIndexNameField(doc, place.getName());
	    
	    if (!place.getAsciiName().equals(place.getName())){
	    	
	    		addIndexNameField(doc, place.getAsciiName());
	    }
	    
	    for (String altName : place.getAlternateNames()){
	    	
	    		if (!altName.equals(place.getName()) && !altName.equals(place.getAsciiName())){
	    	
	    			addIndexNameField(doc, altName);
	    		}
	    }
	    
	    // this is the payload we'll return when matching location
	    // names to gazetteer records
	    addGeonameField(doc, geonameEntry);
	    
	    // TODO: use geonameID to link administrative subdivisions to
	    //		 each other
	    addGeonameIdField(doc, place.getId());
	    
	    // we'll initially sort match results based on population
	    addPopulationField(doc, place.getPopulation());
	    
	    // we'll create a new Spatial geometry from the centroid of the geoname location
	    Shape centroid = spatialContext.makePoint(
	    		place.getCenter().getLongitude(), place.getCenter().getLatitude());
	    
	    // add a deserializable representation of the shape to the document.
	    addGeospatialField(doc, centroid);
	    
	    // we will add the field to the index
	    for (Field f : spatialStrategy.createIndexableFields(centroid)){
	    		
	    		doc.add(f);
	    }
	    
	    return doc;
  	}
  	
  	//LongField populationField = new LongField(FieldConstants.POPULATION, -1l, Field.Store.NO);
  	
  	private void addIndexNameField(Document doc, String value){
  		
  		indexNameField.setStringValue(value);
  		
  		doc.add(indexNameField);  		
  	}
  	
  	private void addGeonameField(Document doc, String geoname){
  		
  		geonameField.setStringValue(geoname);
  		
  		doc.add(geonameField);
  	}
  	
  	private void addGeonameIdField(Document doc, int geonameId){
  		
  		geonameIdField.setIntValue(geonameId);
  		
  		doc.add(geonameIdField);
  	}
  	
  	private void addPopulationField(Document doc, long population){
  		
  		populationField.setLongValue(population);
  		
  		doc.add(populationField);
  	}
  	
  	@SuppressWarnings("deprecation")
	private void addGeospatialField(Document doc, Shape shape){
  		
		// TODO: Maybe do this more elegantly with the Spatial4J API's
  		// ShapeReaderWriter...
  		geospatialField.setStringValue(spatialContext.toString(shape));
  		
  		doc.add(geospatialField);
  	}
}
