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
 * DmsPatternParsingStrategy.java
 * 
 *###################################################################*/

/**
 * Defines the pattern to use for parsing Lat/Lon coordinates specified in
 * Degrees-Minutes-Seconds (DMS) format.
 */
public class DmsPatternParsingStrategy extends BaseDmsPatternParsingStrategy {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DmsPatternParsingStrategy.class);
	
	/**
	 * From: http://en.wikipedia.org/wiki/Geographic_coordinate_conversion
	 * 	#Ways_of_writing_coordinates:
	 * 
	 * This statement satisfies the following patterns listed:
	 *  
	 *  Pattern 1 (ex: 40:26:46.302N 079:56:55.903W)
	 *  Pattern 2 (ex: 40°26′47″N 079°58′36″W or 40°26'47"N 079°58'36"W)
	 *  Pattern 3 (ex: 40d 26′ 47″ N 079d 58′ 36″ W or 40d 26' 47" N 079d 58' 36" W)
	 */
	public static String REGEX_PATTERN = "(" +
			"\\d+" +       // Degrees 
			"\\s?" +       // Whitespace
			"[:°dD]*" +    // Degree Symbol
			"\\s?" +       // Whitespace
			"\\d+" +       // Minutes
			"\\s?" +       // Whitespace
			"[:'′]*" +     // Minutes Symbol
			"\\s?" +       // Whitespace
			"(?:\\d+" +    // Seconds
			"(?:[.]\\d+)?)?" + // Decimal Seconds (optional)  
			"\\s?" +       // Whitespace
			"[\"″]*" +     // Seconds symbol
			"\\s?" +       // Whitespace
			"[NnSs]" +     // Hemisphere
			"\\s?" +       // Whitespace
			"(?:[,;]\\s?)?" +  // Separator
			"\\d+" +       // Degrees
			"\\s?" +       // Whitespace
			"[:°dD]*" +    // Degree Symbol
			"\\s?" +       // Whitespace
			"\\d+" +       // Minutes 
			"\\s?" +       // Whitespace
			"[:'′]*" +     // Minutes Symbol
			"\\s?" +       // Whitespace
			"(?:\\d+" +    // Seconds
			"(?:[.]\\d+)?)?" + // Decimal Seconds (optional)
			"\\s?" +       // Whitespace
			"[\"″]*" +     // Seconds Symbol
			"\\s?" +       // Whitespace
			"[EeWw]" +     // Hemisphere
			")";
	
	public static Pattern CAPTURE_PATTERN = Pattern.compile("(\\d+)\\s?[:°dD]*\\s?(\\d+)\\s?\\s?[:'′]*\\s?(\\d+(?:[.]\\d+)?)?\\s?[:\"″]*\\s?([NnSs])\\s?(?:[,;]\\s?)?(\\d+)\\s?[:°dD]*\\s?(\\d+)\\s?[:'′]*\\s?(\\d+(?:[.]\\d+)?)?\\s?[:\"″]*\\s?([EeWw])");
	
	@Override
	protected String getRegexPattern() {
		
		return REGEX_PATTERN;
	}

	@Override
	protected DegreeMinutesSecondsStringParts extractParts(String matchedString) {
		
		Matcher matches = CAPTURE_PATTERN.matcher(matchedString);
		
		if (matches.find()){
		
			String 	latDeg = null, lonDeg = null, 
					latMin = null, lonMin = null, 
					latSec = null, lonSec = null, 
				    latHemi = null, lonHemi = null;
			
			if (matches.groupCount() == 8){
				
				latDeg = matches.group(1);
				latMin = matches.group(2);
				latSec = (matches.group(3) != null)? matches.group(3) : "0";
				latHemi = matches.group(4);
				lonDeg = matches.group(5);
				lonMin = matches.group(6);
				lonSec = (matches.group(7) != null)? matches.group(7) : "0";
				lonHemi = matches.group(8);
			}
			else {
				
				logger.warn("Unmatched set of capture groups.  Total: {}", 
						matches.groupCount());
			}
			
			boolean isNorth = latHemi.toUpperCase().equals("N");
			boolean isEast = lonHemi.toUpperCase().equals("E");
			
			return new DegreeMinutesSecondsStringParts(
					latDeg, latMin, latSec, isNorth, lonDeg, lonMin, lonSec, isEast);
		}
		
		return null;
	}
}
