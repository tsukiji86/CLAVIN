package com.berico.clavin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.berico.clavin.gazetteer.CountryCode;
import com.berico.clavin.gazetteer.FeatureClass;
import com.berico.clavin.gazetteer.FeatureCode;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.gazetteer.PlaceReference;

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
 * GeonamesUtils.java
 * 
 *###################################################################*/

/**
 * A set of utilities for manipulating Geonames data.
 * 
 */
public class GeonamesUtils {

	/**
	 * Builds a {@link Place} object based on a single gazetteer
	 * record in the GeoNames geographical database.
	 * 
	 * @param inputLine		single line of tab-delimited text representing one record from the GeoNames gazetteer
	 * @return				new Place object
	 */
	public static Place parseFromGeoNamesRecord(String inputLine) {
		
		// GeoNames gazetteer entries are tab-delimited
		String[] tokens = inputLine.split("\t");
		
		// initialize each field with the corresponding token
		int geonameID = Integer.parseInt(tokens[0]);
		String name = tokens[1];
		String asciiName = tokens[2];
		
		List<String> alternateNames;
		if (tokens[3].length() > 0) {
			// better to pass empty array than array containing empty String ""
			alternateNames = Arrays.asList(tokens[3].split(","));
		} else alternateNames = new ArrayList<String>();
		
		double latitude;
		try {
			latitude = Double.parseDouble(tokens[4]);
		} catch (NumberFormatException e) {
			latitude = Place.OUT_OF_BOUNDS;
		}
		
		double longitude;
		try {
			longitude = Double.parseDouble(tokens[5]);
		} catch (NumberFormatException e) {
			longitude = Place.OUT_OF_BOUNDS;
		}
		
		FeatureClass featureClass;
		if (tokens[6].length() > 0) {
			featureClass = FeatureClass.valueOf(tokens[6]);
		} else featureClass = FeatureClass.NULL; // not available
		
		FeatureCode featureCode;
		if (tokens[7].length() > 0) {
			featureCode = FeatureCode.valueOf(tokens[7]);
		} else featureCode = FeatureCode.NULL; // not available
		
		CountryCode primaryCountryCode;
		if (tokens[8].length() > 0) {
			primaryCountryCode = CountryCode.valueOf(tokens[8]);
		} else primaryCountryCode = CountryCode.NULL; // No Man's Land
		
		List<CountryCode> alternateCountryCodes = new ArrayList<CountryCode>();
		if (tokens[9].length() > 0) {
			// don't pass list only containing empty String ""
			for (String code : tokens[9].split(",")) {
				if (code.length() > 0) // check for malformed data
					alternateCountryCodes.add(CountryCode.valueOf(code));
			}
		}
		
		String admin1Code = tokens[10];
		String admin2Code = tokens[11];
		
		String admin3Code;
		String admin4Code;
		long population;
		int elevation;
		//int digitalElevationModel;
		TimeZone timezone;
		Date modificationDate;
		
		// check for dirty data...
		if (tokens.length < 19) {
			// GeoNames record format is corrupted, don't trust any
			// data after this point
			admin3Code = "";
			admin4Code = "";
			population = Place.OUT_OF_BOUNDS;
			elevation = Place.OUT_OF_BOUNDS;
			//digitalElevationModel = Place.OUT_OF_BOUNDS;
			timezone = null;
			modificationDate = new Date(0);
		} else { // everything looks ok, soldiering on...
			admin3Code = tokens[12];
			admin4Code = tokens[13];
			try {
				population = Long.parseLong(tokens[14]);
			} catch (NumberFormatException e) {
				population = Place.OUT_OF_BOUNDS;
			}
			try {
				elevation = Integer.parseInt(tokens[15]);
			} catch (NumberFormatException e) {
				elevation = Place.OUT_OF_BOUNDS;
			}
			// Felt this was unimportant for the purposes of the index.
			/*
			try {
				digitalElevationModel = Integer.parseInt(tokens[16]);
			} catch (NumberFormatException e) {
				digitalElevationModel = Place.OUT_OF_BOUNDS;
			}
			*/
			timezone = TimeZone.getTimeZone(tokens[17]);
			try {
				modificationDate = new SimpleDateFormat("yyyy-MM-dd").parse(tokens[18]);
			} catch (ParseException e) {
				modificationDate = new Date(0);
			}
		}
		
		Place p = new Place();
		
		p.setId(geonameID);
		p.setName(name);
		p.setAsciiName(asciiName);
		p.setAlternateNames(alternateNames);
		p.setCenter(new LatLon(latitude, longitude));
		p.setFeatureClass(featureClass);
		p.setFeatureCode(featureCode);
		p.setPrimaryCountryCode(primaryCountryCode);
		p.setAlternateCountryCodes(alternateCountryCodes);
		
		ArrayList<PlaceReference> superPlaces = new ArrayList<PlaceReference>();
		
		// TODO: Resolve Admin Code IDs
		superPlaces.add(new PlaceReference(admin1Code, admin1Code, true));
		superPlaces.add(new PlaceReference(admin2Code, admin2Code, true));
		superPlaces.add(new PlaceReference(admin3Code, admin3Code, true));
		superPlaces.add(new PlaceReference(admin4Code, admin4Code, true));
		
		p.setSuperPlaces(superPlaces);
		
		p.setPopulation(population);
		p.setElevation(elevation);
		p.setTimezone(timezone);
		p.setModificationDate(modificationDate);
		p.setContext(inputLine);
		
		return p;
	}
}
