package com.berico.clavin.gazetteer;

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
import java.util.List;
import java.util.TimeZone;

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
 * 		http://www.apache.org/licenses/LICENSE-2.0
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
 * into corresponding {@link Place} objects.
 *
 */
public class GeoNameTest {

	/**
	 * Parse a bunch of gazetteer records and make sure we're building
	 * the correct {@link Place} objects.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void testParseFromGeoNamesRecord() throws IOException, ParseException {
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
				new File("./src/test/resources/gazetteers/GeoNamesSampleSet.txt")), "UTF-8"));
		String line;
		ArrayList<Place> geonames = new ArrayList<Place>();
		while ((line = r.readLine()) != null)
			geonames.add(GeonamesUtils.parseFromGeoNamesRecord(line));
		
		Place geoname;
		geoname = geonames.get(0); // standard US city
		assertEquals("incorrect geonameID", 4781530, geoname.id);
		assertEquals("incorrect name", "Reston", geoname.name);
		assertEquals("incorrect asciiName", "Reston", geoname.asciiName);
		assertEquals("incorrect alternateNames", Arrays.asList("Reston","Рестон"), geoname.alternateNames);
		assertEquals("incorrect latitude", 38.96872, geoname.getCenter().getLatitude(), 0.1);
		assertEquals("incorrect longitude", -77.3411, geoname.getCenter().getLongitude(), 0.1);
		assertEquals("incorrect featureClass", FeatureClass.P, geoname.featureClass);
		assertEquals("incorrect featureCode", FeatureCode.PPL, geoname.featureCode);
		assertEquals("incorrect primaryCountryCode", CountryCode.US, geoname.primaryCountryCode);
		assertEquals("incorrect primaryCountryName", CountryCode.US.name, geoname.getPrimaryCountryCode().name);
		assertEquals("incorrect alternateCountryCodes", new ArrayList<CountryCode>(), geoname.alternateCountryCodes);
		
		List<PlaceReference> adminParents = geoname.getAdministrativeParents();
		
		String a1 = adminParents.get(0).getName();
		String a2 = adminParents.get(1).getName();
		String a3 = adminParents.get(2).getName();
		String a4 = adminParents.get(3).getName();
		
		assertEquals("incorrect adminCode1", "VA", a1);
		assertEquals("incorrect adminCode2", "059", a2);
		assertEquals("incorrect adminCode3", "", a3);
		assertEquals("incorrect adminCode4", "", a4);
		assertEquals("incorrect population", 58404, geoname.getPopulation());
		assertEquals("incorrect elevation", 100.0, geoname.getElevation(), 1.0);
		assertEquals("incorrect timezone", TimeZone.getTimeZone("America/New_York"), geoname.timezone);
		assertEquals("incorrect modificationDate", new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-14"), geoname.modificationDate);
		
		geoname = geonames.get(1); // lots of UTF chars & missing columns
		assertEquals("incorrect geonameID", 1139905, geoname.id);
		assertEquals("incorrect name", "Ḩowẕ-e Ḩājī Bēg", geoname.name);
		assertEquals("incorrect asciiName", "Howz-e Haji Beg", geoname.asciiName);
		assertEquals("incorrect alternateNames", Arrays.asList("Hawdze Hajibeg","Howz-e Haji Beg","Howz-e Hajjibeyg","H̱awdze Ḩājibeg","حوض حاجی بېگ","Ḩowẕ-e Ḩājjībeyg","Ḩowẕ-e Ḩājī Bēg"), geoname.alternateNames);
		assertEquals("incorrect latitude", 34.90489, geoname.getCenter().getLatitude(), 0.1);
		assertEquals("incorrect longitude", 64.10312, geoname.getCenter().getLongitude(), 0.1);
		
		geoname = geonames.get(2); // seldom-used fields
		assertEquals("incorrect geonameID", 2826158, geoname.id);
		assertEquals("incorrect alternateNames", new ArrayList<String>(), geoname.alternateNames);
		
		// TODO: Figure out the geonames strategy for 3, 4 labels since we now support much longer
		// hierarchies.
		//assertEquals("incorrect adminCode3", "07138", geoname.admin3Code);
		//assertEquals("incorrect adminCode4", "07138071", geoname.admin4Code);
		
		geoname = geonames.get(3); // no primaryCountryCode
		assertEquals("incorrect primaryCountryCode", CountryCode.NULL, geoname.primaryCountryCode);
		
		geoname = geonames.get(4); // non-empty alternateCountryCodes
		assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.US, CountryCode.MX), geoname.alternateCountryCodes);
		
		geoname = geonames.get(5); // malformed alternateCountryCodes
		assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.PS), geoname.alternateCountryCodes);
		
		geoname = geonames.get(6); // no featureCode
		assertEquals("incorrect featureClass", FeatureCode.NULL, geoname.featureCode);
		
		r.close();
	}

}
