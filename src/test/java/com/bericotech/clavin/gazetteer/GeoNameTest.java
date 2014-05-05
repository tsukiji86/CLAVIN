package com.bericotech.clavin.gazetteer;

import static org.junit.Assert.*;

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
import org.junit.Before;
import org.junit.Test;

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
    private GeoName reston;
    private GeoName howzEHaji;
    private GeoName strabenhaus;
    private GeoName noMansLand;
    private GeoName chihuahuaDesert;
    private GeoName rasSalim;
    private GeoName murrayCanyon;
    private GeoName boston;
    private GeoName gunBarrelCity;
    private GeoName unitedStates;
    private GeoName fairfaxCounty;
    private GeoName virginia;
    private GeoName antarctica;
    private GeoName coralSeaIslands;
    private GeoName campoParish;
    private GeoName americanSamoa;
    private GeoName australia;

    @Before
    public void setUp() throws IOException {
        // load GeoNames.org sample data file & instantiate corresponding GeoName objects
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File("./src/test/resources/gazetteers/GeoNamesSampleSet.txt")), "UTF-8"));
        String line;
        ArrayList<GeoName> geonames = new ArrayList<GeoName>();
        while ((line = r.readLine()) != null) {
            geonames.add(GeoName.parseFromGeoNamesRecord(line));
        }
        r.close();

        reston = geonames.get(0);
        howzEHaji = geonames.get(1);
        strabenhaus = geonames.get(2);
        noMansLand = geonames.get(3);
        chihuahuaDesert = geonames.get(4);
        rasSalim = geonames.get(5);
        murrayCanyon = geonames.get(6);
        boston = geonames.get(7);
        gunBarrelCity = geonames.get(8);
        unitedStates = geonames.get(9);
        fairfaxCounty = geonames.get(10);
        virginia = geonames.get(11);
        antarctica = geonames.get(12);
        coralSeaIslands = geonames.get(13);
        campoParish = geonames.get(14);
        americanSamoa = geonames.get(15);
        australia = geonames.get(16);
    }

    /**
     * Test all attributes for a standard US city.
     * @throws ParseException if an error occurs parsing the test date
     */
    @Test
    public void testAllAttributes() throws ParseException {
        assertEquals("incorrect geonameID", 4781530, reston.getGeonameID());
        assertEquals("incorrect name", "Reston", reston.getName());
        assertEquals("incorrect asciiName", "Reston", reston.getAsciiName());
        assertEquals("incorrect alternateNames", Arrays.asList("Reston","Рестон"), reston.getAlternateNames());
        assertEquals("incorrect latitude", 38.96872, reston.getLatitude(), 0.1);
        assertEquals("incorrect longitude", -77.3411, reston.getLongitude(), 0.1);
        assertEquals("incorrect featureClass", FeatureClass.P, reston.getFeatureClass());
        assertEquals("incorrect featureCode", FeatureCode.PPL, reston.getFeatureCode());
        assertEquals("incorrect primaryCountryCode", CountryCode.US, reston.getPrimaryCountryCode());
        assertEquals("incorrect primaryCountryName", CountryCode.US.name, reston.getPrimaryCountryName());
        assertEquals("incorrect alternateCountryCodes", new ArrayList<CountryCode>(), reston.getAlternateCountryCodes());
        assertEquals("incorrect adminCode1", "VA", reston.getAdmin1Code());
        assertEquals("incorrect adminCode2", "059", reston.getAdmin2Code());
        assertEquals("incorrect adminCode3", "", reston.getAdmin3Code());
        assertEquals("incorrect adminCode4", "", reston.getAdmin4Code());
        assertEquals("incorrect population", 58404, reston.getPopulation());
        assertEquals("incorrect elevation", 100, reston.getElevation());
        assertEquals("incorrect digitalElevationModel", 102, reston.getDigitalElevationModel());
        assertEquals("incorrect timezone", TimeZone.getTimeZone("America/New_York"), reston.getTimezone());
        assertEquals("incorrect modificationDate", new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-14"), reston.getModificationDate());
        assertEquals("incorrect parent ancestry key", "US.VA.059", reston.getParentAncestryKey());
        assertNull("ancestry key should be null", reston.getAncestryKey());
    }

    /**
     * Test UTF characters and missing columns.
     */
    @Test
    public void testUTFAndMissingColumns() {
        assertEquals("incorrect geonameID", 1139905, howzEHaji.getGeonameID());
        assertEquals("incorrect name", "Ḩowẕ-e Ḩājī Bēg", howzEHaji.getName());
        assertEquals("incorrect asciiName", "Howz-e Haji Beg", howzEHaji.getAsciiName());
        assertEquals("incorrect alternateNames", Arrays.asList("Hawdze Hajibeg","Howz-e Haji Beg","Howz-e Hajjibeyg","H̱awdze Ḩājibeg","حوض حاجی بېگ","Ḩowẕ-e Ḩājjībeyg","Ḩowẕ-e Ḩājī Bēg"), howzEHaji.getAlternateNames());
        assertEquals("incorrect latitude", 34.90489, howzEHaji.getLatitude(), 0.1);
        assertEquals("incorrect longitude", 64.10312, howzEHaji.getLongitude(), 0.1);
    }

    /**
     * Test seldom used fields.
     */
    @Test
    public void testSeldomUsedFields() {
        assertEquals("incorrect geonameID", 2826158, strabenhaus.getGeonameID());
        assertEquals("incorrect alternateNames", new ArrayList<String>(), strabenhaus.getAlternateNames());
        assertEquals("incorrect adminCode3", "07138", strabenhaus.getAdmin3Code());
        assertEquals("incorrect adminCode4", "07138071", strabenhaus.getAdmin4Code());
        assertEquals("incorrect ancestry key", "DE.08.00.07138.07138071", strabenhaus.getAncestryKey());
        assertEquals("incorrect parent ancestry key", "DE.08.00.07138", strabenhaus.getParentAncestryKey());
    }

    /**
     * Test no primary country code.
     */
    @Test
    public void testNoPrimaryCountryCode() {
        assertEquals("incorrect primaryCountryCode", CountryCode.NULL, noMansLand.getPrimaryCountryCode());
        assertNull("if no country code, ancestry path should be null", noMansLand.getParentAncestryKey());
        assertTrue("ancestry should be resolved", noMansLand.isAncestryResolved());
    }

    /**
     * Test non-empty alternate country codes.
     */
    @Test
    public void testNonEmptyAlternateCountryCodes() {
        assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.US, CountryCode.MX),
                chihuahuaDesert.getAlternateCountryCodes());
    }

    /**
     * Test malformed alternate country codes.
     */
    @Test
    public void testMalformedAlternateCountryCodes() {
        assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.PS), rasSalim.getAlternateCountryCodes());
    }

    /**
     * Test no feature code.
     */
    @Test
    public void testNoFeatureCode() {
        assertEquals("incorrect featureCode", FeatureCode.NULL, murrayCanyon.getFeatureCode());
        assertNotNull("parent ancestry key should not be null", murrayCanyon.getParentAncestryKey());
    }

    /**
     * Test ancestry keys for top-level administrative division.
     */
    @Test
    public void testTopLevelAncestryKeys() {
        assertEquals("incorrect ancestry key", "US", unitedStates.getAncestryKey());
        assertNull("parent ancestry key should be null", unitedStates.getParentAncestryKey());
    }

    /**
     * Test isToplevelTerritory.
     */
    @Test
    public void testIsTopLevelTerritory() {
        assertFalse("Non-territory [Gun Barrel City] should not be a top level territory", gunBarrelCity.isTopLevelTerritory());
        assertTrue("[Antarctica] should be a top level territory", antarctica.isTopLevelTerritory());
        assertTrue("[American Samoa] should be a top level territory", americanSamoa.isTopLevelTerritory());
        assertFalse("[Coral Sea Islands] should not be a top level territory", coralSeaIslands.isTopLevelTerritory());
    }

    /**
     * Test correct ancestry keys for territories.
     */
    @Test
    public void testTerritoryAncestryKeys() {
        assertEquals("incorrect ancestry key for top-level territory", "AQ", antarctica.getAncestryKey());
        assertNull("parent ancestry key of top-level territory should be null", antarctica.getParentAncestryKey());

        assertEquals("incorrect ancestry key for top-level territory", "AS", americanSamoa.getAncestryKey());
        assertNull("parent ancestry key of top-level territory should be null", americanSamoa.getParentAncestryKey());

        assertNull("ancestry key for non-top-level territory should be null", coralSeaIslands.getAncestryKey());
        assertEquals("incorrect parent ancestry key for non-top-level territory", "AU", coralSeaIslands.getParentAncestryKey());
    }

    /**
     * Test correct ancestry keys for parishes.
     */
    @Test
    public void testParishAncestryKeys() {
        assertNull("ancestry key for parish should be null", campoParish.getAncestryKey());
        assertEquals("incorrect parent ancestry key for parish", "ES", campoParish.getParentAncestryKey());
    }

    /**
     * Test ancestry resolution check when no parents are resolved.
     */
    @Test
    public void testIsAncestryResolved_NoParents() {
        // verify correct resolution response when no parents are set
        assertFalse("no parent set [reston], should not be resolved", reston.isAncestryResolved());
        assertFalse("no parent set [fairfax county], should not be resolved", fairfaxCounty.isAncestryResolved());
        assertFalse("no parent set [virginia], should not be resolved", virginia.isAncestryResolved());
        assertTrue("no parent set [united states] but top level, should be resolved", unitedStates.isAncestryResolved());
        assertTrue("no parent set [american samoa] but top level, should be resolved", americanSamoa.isAncestryResolved());
        assertFalse("no parent set [coral sea islands], should not be resolved", coralSeaIslands.isAncestryResolved());
    }

    /**
     * Test ancestry resolution when only part of the ancestry is resolved.
     */
    @Test
    public void testIsAncestryResolved_PartialResolution() {
        reston.setParent(fairfaxCounty);
        assertFalse("only one parent set [reston], should not be resolved", reston.isAncestryResolved());
        fairfaxCounty.setParent(virginia);
        assertFalse("only two parents set [reston], should not be resolved", reston.isAncestryResolved());
        assertFalse("only one parent set [fairfax county], should not be resolved", fairfaxCounty.isAncestryResolved());
    }

    /**
     * Test ancestry resolution when all ancestry is resolved.
     */
    @Test
    public void testIsAncestryResolved_FullResolution() {
        virginia.setParent(unitedStates);
        fairfaxCounty.setParent(virginia);
        reston.setParent(fairfaxCounty);
        assertTrue("[reston] should be fully resolved", reston.isAncestryResolved());
        assertTrue("[fairfax county] should be fully resolved", fairfaxCounty.isAncestryResolved());
        assertTrue("[virginia] should be fully resolved", virginia.isAncestryResolved());
        assertTrue("[united states] should be fully resolved", unitedStates.isAncestryResolved());

        coralSeaIslands.setParent(australia);
        assertTrue("[coral sea islands] should be fully resolved", coralSeaIslands.isAncestryResolved());
    }

    /**
     * Test invalid parent configuration.
     */
    @Test
    public void testSetParent_InvalidParent() {
        assertNull("[reston] should have no parent", reston.getParent());
        assertFalse("non-administrative parent should not be allowed", reston.setParent(boston));
        assertNull("[reston] should have no parent", reston.getParent());
        assertFalse("non-direct parent should not be allowed", reston.setParent(virginia));
        assertNull("[reston] should have no parent", reston.getParent());
        assertTrue("direct parent should be allowed", reston.setParent(fairfaxCounty));
        assertEquals("[reston] should have parent [fairfax county]", fairfaxCounty, reston.getParent());
        assertFalse("null parent should result in a no-op", reston.setParent(null));
        assertEquals("[reston] should have parent [fairfax county]", fairfaxCounty, reston.getParent());
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
