package com.berico.clavin.extractor.coords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * DdHemiLetterPatternParsingStrategy.java
 * 
 *###################################################################*/

/**
 * Defines the pattern to use for parsing Lat/Lon coordinates specified in
 * Decimal Degrees (DD) format with a Letter for a Hemisphere designation.
 */
public class DdHemiLetterPatternParsingStrategy extends BaseDdPatternParsingStrategy {

	private static final Logger logger = LoggerFactory.getLogger(DdHemiLetterPatternParsingStrategy.class);
	
	/**
	 * From: http://en.wikipedia.org/wiki/Geographic_coordinate_conversion
	 * 	#Ways_of_writing_coordinates:
	 * 
	 * This statement satisfies the following patterns listed:
	 *  
	 *  Pattern 2 (ex: 40.446195N 79.948862W)
	 *  as well as, 40.446195N, 79.948862W
	 */
	public static String REGEX_PATTERN = "(" +
			"\\d+" +    // Characteristic (integer part)
			"[.]" +     // Decimal
			"\\d+" +    // Mantissa (decimal part)
			"[NnSs]" +  // Latitudinal Hemisphere
			"[,]?" +    // Separator (optional)
			"\\s+" +    // Whitespace
			"\\d+" +    // Characteristic (integer part)
			"[.]" +     // Decimal
			"\\d+" +    // Mantissa (decimal part)
			"[EeWw]" +  // Longitudinal Hemisphere
			")";
	
	
	public static Pattern CAPTURE_PATTERN = Pattern.compile("(\\d+[.]\\d+)([NnSs])[,]?\\s+(\\d+[.]\\d+)([EeWw])");
	
	/**
	 * Get the Regex expression.
	 * @return Regex expression.
	 */
	@Override
	protected String getRegexPattern() {
		
		return REGEX_PATTERN;
	}
	
	/**
	 * Given the matching string, extract the string parts necessary to 
	 * convert the coordinate to it's LatLon representation.
	 * @param matchedString The string the matched the primary Regex pattern.
	 * @return A simple parsed structure representing the coordinate.
	 */
	@Override
	protected DecimalDegreeStringParts extractParts(String matchedString) {
		
		Matcher matches = CAPTURE_PATTERN.matcher(matchedString);
		
		matches.find();
		
		String latitude = matches.group(1);
		
		logger.trace("Extracted latitude: {}", latitude);
		
		String latHemi = matches.group(2);
		
		logger.trace("Extracted LatHemi: {}", latHemi);
		
		String longitude = matches.group(3);
		
		logger.trace("Extracted longitude: {}", longitude);
		
		String lonHemi = matches.group(4);
		
		logger.trace("Extracted LonHemi: {}", lonHemi);
		
		boolean isNorth = latHemi.equalsIgnoreCase("N");
		
		boolean isEast = lonHemi.equalsIgnoreCase("E");
		
		return new DecimalDegreeStringParts(latitude, longitude, isNorth, isEast);
	}
}
