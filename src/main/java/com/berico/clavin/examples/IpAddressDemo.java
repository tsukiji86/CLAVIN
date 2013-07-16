package com.berico.clavin.examples;

import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;

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
 * IpAddressDemo.java
 * 
 *###################################################################*/

/**
 * Demonstrates the extraction and resolution of IP Addresses.
 * TODO: Scope resolution to administrative districts.
 */
public class IpAddressDemo {

	
	public static void main(String[] args) throws Exception {
		
		// Register the new Parsing Strategy.
		GeoParserFactory
			.DefaultCoordinateParsingStrategies
				.add(new IpAddressParsingStrategy());
		
		// Get a parser instance.
		GeoParser parser = GeoParserFactory.getDefault("IndexDirectory/");
		
		// Extract and resolve IP Addresses and coordinates from the text below.
		ResolutionContext results = 
			parser.parse("This is a test 34.55.44.22 and here is another " +
					"one 23.1.24.255, and both are near 12.345N, 110.123W.");
		
		// Demonstrate the extraction results
		for (CoordinateOccurrence<?> coord : 
			results.getExtractionContext().getCoordinates()){
			
			System.out.println(String.format("%s (%s) was extracted as %s, %s.", 
					coord.getExtractedText(),
					coord.getCoordinateSystem(),
					coord.convertToLatLon().getLatitude(), 
					coord.convertToLatLon().getLongitude()));
		}
		
		// Demonstrate the resolved results
		for (ResolvedCoordinate coord : results.getCoordinates()){
			
			System.out.println(String.format("%s (%s) was resolved as %s, %s.", 
					coord.getOccurrence().getExtractedText(),
					coord.getOccurrence().getCoordinateSystem(),
					coord.getKnownLocation().getName(),
					coord.getKnownLocation().getPrimaryCountryCode().name));
		}
	}
}
