package com.berico.clavin.extractor.coords;

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


public class DmsPatternParsingStrategy extends BaseDmsPatternParsingStrategy {
	
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
	public static String REGEX_PATTERN = 
		"(" + // Capture Start 
		"(?<latdeg>\\d+)" + // Latitude Degrees
		"\\s?[:°dD]*\\s?" +
		"(?<latmin>\\d+)\\s?" + // Latitude Minutes
		"\\s?[:'′]*\\s?" +
		"(?<latsec>\\d+([.]\\d+)?)?" + // (optional) Latitude Seconds
		"\\s?[:\"″]*\\s?" +
		"(?<lathemi>[NnSs])\\s?" + // North or South
		"([,;]\\s?)?" + // Latitude/Longitude Barrier
		"(?<londeg>\\d+)" + // Longitude Degrees
		"\\s?[:°dD]*\\s?" +
		"(?<lonmin>\\d+)" + // Longitude Minutes
		"\\s?[:'′]*\\s?" +
		"(?<lonsec>\\d+([.]\\d+)?)?" + // (optional) Longitude Seconds
		"\\s?[:\"″]*\\s?" +
		"(?<lonhemi>[EeWw])" + // East or West
		")"; // Capture End

	@Override
	protected String getRegexPattern() {
		
		return REGEX_PATTERN;
	}
}
