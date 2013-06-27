package com.berico.clavin.extractor.coords;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.LatLon;

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
 * BaseDmsPatternParsingStrategy.java
 * 
 *###################################################################*/

/**
 * If you find some other way to parse a Degree-Minute-Second string that does
 * not conform to the Regex in {@link DmsPatternParsingStrategy}, extend this,
 * use the named capturing groups provided below in your Regex statement, 
 * and this class will the rest of the work.
 */
public abstract class BaseDmsPatternParsingStrategy 
		extends BaseRegexPatternParsingStrategy
		implements RegexCoordinateParsingStrategy<LatLon> {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(BaseDmsPatternParsingStrategy.class);
	
	/**
	 * Parse the matchedString returning a lat/lon occurrence.  This
	 * implementation relies on named capture groups:
	 * 
	 * latdeg: Degrees latitude
	 * latmin: Minutes latitude
	 * latsec: Seconds latitude
	 * lathemi: Hemisphere of latitude
	 * londeg: Degrees longitude
	 * lonmin: Minutes longitude
	 * lonsec: Seconds longitude
	 * lonhemi: Hemisphere of longitude
	 * 
	 * @param matchedString String representing the lat/lon matched by
	 * the Regex statement.
	 * @param namedGroups A map of name capture groups with their values.
	 * @param startPosition Position where the extraction occurred in text.
	 * This is needed for creating an Occurrence object.
	 * @return A lat/lon coordinate occurrence.
	 */
	@Override
	public CoordinateOccurrence<LatLon> parse(
			String matchedString, Map<String, String> namedGroups, int startPosition) {
		
		double latitude = convertToDecimal(
				namedGroups.get("latdeg"), 
				namedGroups.get("latmin"), 
				namedGroups.get("latsec"), 
				namedGroups.get("lathemi"));
		
		double longitude = convertToDecimal(
				namedGroups.get("londeg"), 
				namedGroups.get("lonmin"), 
				namedGroups.get("lonsec"), 
				namedGroups.get("lonhemi"));
		
		logger.debug("From '{}', parsed Lat/Lon: {}, {}.", 
				new Object[]{ matchedString, latitude, longitude });
		
		LatLon latlon = new LatLon(latitude, longitude);
		
		return new LatLonOccurrence(matchedString, startPosition, latlon);
	}
	
	/**
	 * Convert the degree, minute, second representation of a latitude
	 * or longitude into it's decimal representation.
	 * 
	 * @param degrees Degrees latitude or longitude.
	 * @param minutes Minutes latitude or longitude.
	 * @param seconds Seconds latitude or longitude (optionally a decimal value
	 * for subseconds).
	 * @param hemisphere Hemisphere of the latitude or longitude.
	 * @return Decimal representation of the latitude or longitude.
	 */
	public static double convertToDecimal(
		String degrees, String minutes, String seconds, String hemisphere){
		
		int hemi = hemisphere.toUpperCase().equalsIgnoreCase("N")
			  || hemisphere.toUpperCase().equalsIgnoreCase("E") ? 1 : -1;
		
		int deg = tryParse(degrees, 0);
		int min = tryParse(minutes, 0);
		double sec = tryParse(seconds, 0.0d);
		
		return hemi * (deg + min / 60.0 + sec / 3600.0);
	}
}
