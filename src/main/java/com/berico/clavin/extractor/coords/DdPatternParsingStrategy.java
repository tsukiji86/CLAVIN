package com.berico.clavin.extractor.coords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * DdPatternParsingStrategy.java
 * 
 *###################################################################*/

/**
 * Defines the pattern to use for parsing Lat/Lon coordinates specified in
 * Decimal Degrees (DD) format.
 */
public class DdPatternParsingStrategy extends BaseDdPatternParsingStrategy {

	/**
	 * From: http://en.wikipedia.org/wiki/Geographic_coordinate_conversion
	 * 	#Ways_of_writing_coordinates:
	 * 
	 * This statement satisfies the following patterns listed:
	 *  
	 *  Pattern 1 (ex: -23.399437,-52.090904 or 40.446195, -79.948862)
	 *  Pattern 2 (ex: 40.446195N 79.948862W)
	 */
	public static String REGEX_PATTERN = "(" +
			"-?" +    // A negative sign (optional)
			"\\d+" +  // Characteristic (integer before decimal)
			"[.]" +   // Decimal
			"\\d+" +  // Mantissa (float after decimal)
			"," +     // Separator
			"\\s*" +  // Whitespace
			"-?" +    // A negative sign (optional)
			"\\d+" +  // Characteristic (integer before decimal)
			"[.]" +   // Decimal
			"\\d+" +  // Mantissa (float after decimal)
			")";
	
	public static Pattern CAPTURE_PATTERN = Pattern.compile(
		"(-?\\d+[.]\\d+)" +  // Capture first decimal
		",\\s*" +
		"(-?\\d+[.]\\d+)"    // Capture second decimal
	);
	
	
	@Override
	protected String getRegexPattern() {
		
		return REGEX_PATTERN;
	}

	@Override
	protected DecimalDegreeStringParts extractParts(String matchedString) {
		
		Matcher matches = CAPTURE_PATTERN.matcher(matchedString);
		
		//matches
	}

}
