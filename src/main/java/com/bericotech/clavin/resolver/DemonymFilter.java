package com.bericotech.clavin.resolver;

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
 * DemonymFilter.java
 *
 *###################################################################*/

import com.bericotech.clavin.extractor.LocationOccurrence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Various named entity recognizers tend to mistakenly extract demonyms
 * (i.e., names for residents of localities (e.g., American, British))
 * as place names, which tends to gum up the works for the
 * {@link ClavinLocationResolver}, so this class filters them out from
 * the list of
 * {@link com.bericotech.clavin.extractor.LocationOccurrence}s passed
 * to the resolver.
 *
 */
public class DemonymFilter {
    private static HashSet<String> demonyms;

    public static boolean isDemonym(LocationOccurrence extractedLocation) {
        // lazy load set of demonyms
        if (demonyms == null) {
            // populate set of demonyms to filter out from results, source:
            // http://en.wikipedia.org/wiki/List_of_adjectival_and_demonymic_forms_for_countries_and_nations
            demonyms = new HashSet<String>();

            BufferedReader br = new BufferedReader(new InputStreamReader(DemonymFilter.class.getClassLoader().getResourceAsStream("Demonyms.txt")));

            String line;
            try {
                while ((line = br.readLine()) != null)
                    demonyms.add(line);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return demonyms.contains(extractedLocation.getText());
    }
}
