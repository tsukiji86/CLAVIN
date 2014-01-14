package com.bericotech.clavin.gazetteer;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

import org.junit.Test;

import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.gazetteer.FeatureClass;
import com.bericotech.clavin.gazetteer.FeatureCode;
import com.bericotech.clavin.gazetteer.GeoName;

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
 * GeoNameTest.java
 * 
 *###################################################################*/

/**
 * Tests to make sure GeoNames gazetteer records are properly parsed
 * into corresponding {@link GeoName} objects.
 *
 */
public class GeoNameTest {

    /**
     * Parse a bunch of gazetteer records and make sure we're building
     * the correct {@link GeoName} objects.
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testParseFromGeoNamesRecord() throws IOException, ParseException {
        // load GeoNames.org sample data file & instantiate corresponding GeoName objects
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File("./src/test/resources/gazetteers/GeoNamesSampleSet.txt")), "UTF-8"));
        String line;
        ArrayList<GeoName> geonames = new ArrayList<GeoName>();
        while ((line = r.readLine()) != null)
            geonames.add(GeoName.parseFromGeoNamesRecord(line));
        r.close();
        
        // check all attributed for a standard US city
        GeoName geoname;
        geoname = geonames.get(0);
        assertEquals("incorrect geonameID", 4781530, geoname.getGeonameID());
        assertEquals("incorrect name", "Reston", geoname.getName());
        assertEquals("incorrect asciiName", "Reston", geoname.getAsciiName());
        assertEquals("incorrect alternateNames", Arrays.asList("Reston","Рестон"), geoname.getAlternateNames());
        assertEquals("incorrect latitude", 38.96872, geoname.getLatitude(), 0.1);
        assertEquals("incorrect longitude", -77.3411, geoname.getLongitude(), 0.1);
        assertEquals("incorrect featureClass", FeatureClass.P, geoname.getFeatureClass());
        assertEquals("incorrect featureCode", FeatureCode.PPL, geoname.getFeatureCode());
        assertEquals("incorrect primaryCountryCode", CountryCode.US, geoname.getPrimaryCountryCode());
        assertEquals("incorrect primaryCountryName", CountryCode.US.name, geoname.getPrimaryCountryName());
        assertEquals("incorrect alternateCountryCodes", new ArrayList<CountryCode>(), geoname.getAlternateCountryCodes());
        assertEquals("incorrect adminCode1", "VA", geoname.getAdmin1Code());
        assertEquals("incorrect adminCode2", "059", geoname.getAdmin2Code());
        assertEquals("incorrect adminCode3", "", geoname.getAdmin3Code());
        assertEquals("incorrect adminCode4", "", geoname.getAdmin4Code());
        assertEquals("incorrect population", 58404, geoname.getPopulation());
        assertEquals("incorrect elevation", 100, geoname.getElevation());
        assertEquals("incorrect digitalElevationModel", 102, geoname.getDigitalElevationModel());
        assertEquals("incorrect timezone", TimeZone.getTimeZone("America/New_York"), geoname.getTimezone());
        assertEquals("incorrect modificationDate", new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-14"), geoname.getModificationDate());
        
        // lots of UTF chars & missing columns
        geoname = geonames.get(1);
        assertEquals("incorrect geonameID", 1139905, geoname.getGeonameID());
        assertEquals("incorrect name", "Ḩowẕ-e Ḩājī Bēg", geoname.getName());
        assertEquals("incorrect asciiName", "Howz-e Haji Beg", geoname.getAsciiName());
        assertEquals("incorrect alternateNames", Arrays.asList("Hawdze Hajibeg","Howz-e Haji Beg","Howz-e Hajjibeyg","H̱awdze Ḩājibeg","حوض حاجی بېگ","Ḩowẕ-e Ḩājjībeyg","Ḩowẕ-e Ḩājī Bēg"), geoname.getAlternateNames());
        assertEquals("incorrect latitude", 34.90489, geoname.getLatitude(), 0.1);
        assertEquals("incorrect longitude", 64.10312, geoname.getLongitude(), 0.1);
        
        // seldom-used fields
        geoname = geonames.get(2);
        assertEquals("incorrect geonameID", 2826158, geoname.getGeonameID());
        assertEquals("incorrect alternateNames", new ArrayList<String>(), geoname.getAlternateNames());
        assertEquals("incorrect adminCode3", "07138", geoname.getAdmin3Code());
        assertEquals("incorrect adminCode4", "07138071", geoname.getAdmin4Code());
        
        // no primaryCountryCode
        geoname = geonames.get(3);
        assertEquals("incorrect primaryCountryCode", CountryCode.NULL, geoname.getPrimaryCountryCode());
        
        // non-empty alternateCountryCodes
        geoname = geonames.get(4);
        assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.US, CountryCode.MX), geoname.getAlternateCountryCodes());
        
        // malformed alternateCountryCodes
        geoname = geonames.get(5);
        assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.PS), geoname.getAlternateCountryCodes());
        
        // no featureCode
        geoname = geonames.get(6);
        assertEquals("incorrect featureClass", FeatureCode.NULL, geoname.getFeatureCode());
    }
    
    /**
     * Parse a "bad" gazetteer record to make sure exceptions are
     * properly caught and handled.
     * 
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testParseExceptions() throws IOException, ParseException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File("./src/test/resources/gazetteers/BadGeoNamesSample.txt")), "UTF-8"));
        String line;
        ArrayList<GeoName> geonames = new ArrayList<GeoName>();
        while ((line = r.readLine()) != null)
            geonames.add(GeoName.parseFromGeoNamesRecord(line));
        r.close();
        
        // if no exceptions are thrown, the test is assumed to have succeeded
    }

}
