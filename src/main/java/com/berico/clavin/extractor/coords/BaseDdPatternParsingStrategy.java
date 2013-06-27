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
 * BaseDdPatternParsingStrategy.java
 * 
 *###################################################################*/

/**
 * If you find some other way to parse a Decimal-degree string that does
 * not conform to the Regex in {@link DdPatternParsingStrategy}, extend this,
 * use the named capturing groups provided below in your Regex statement, 
 * and this class will the rest of the work.
 */
public abstract class BaseDdPatternParsingStrategy
		extends BaseRegexPatternParsingStrategy
		implements RegexCoordinateParsingStrategy<LatLon>  {

	private static final Logger logger = 
			LoggerFactory.getLogger(BaseDdPatternParsingStrategy.class);
	
	/**
	 * Parse the matchedString returning a lat/lon occurrence.  This
	 * implementation relies on named capture groups:
	 * 
	 * latdd: Decimal Degrees latitude
	 * lathemi: (optional) Hemisphere of latitude
	 * londd: Decimal Degrees longitude
	 * lonhemi: (optional) Hemisphere of longitude
	 * 
	 * @param matchedString String representing the lat/lon matched by
	 * the Regex statement.
	 * @param namedGroups A map of name capture groups with their values.
	 * @param startPosition Position where the extraction occurred in text.
	 * This is needed for creating an Occurrence object.
	 * @return A lat/lon coordinate occurrence.
	 */
	@Override
	public CoordinateOccurrence<LatLon> parse(String matchedString,
			Map<String, String> namedGroups, int startPosition) {
		
		double latitude = parseDecimalDegrees(
				namedGroups.get("latdd"),
				namedGroups.get("lathemi"));
		
		double longitude = parseDecimalDegrees(
				namedGroups.get("londd"),
				namedGroups.get("lonhemi"));
		
		logger.debug("From '{}', parsed Lat/Lon: {}, {}.", 
				new Object[]{ matchedString, latitude, longitude });
		
		LatLon latlon = new LatLon(latitude, longitude);
		
		return new LatLonOccurrence(startPosition, matchedString, latlon);
	}
	
	/**
	 * Parse the decimal degree representation of a latitude
	 * or longitude from a string.
	 * 
	 * @param decimalDegrees Decimal representation of the coordinate.
	 * @param hemisphere Hemisphere of the latitude or longitude.
	 * @return Decimal representation of the latitude or longitude.
	 */
	public double parseDecimalDegrees(String decimalDegrees, String hemisphere){
		
		int hemi = 1;
		
		if (hemisphere != null && !hemisphere.trim().isEmpty())
			hemi = hemisphere.toUpperCase().equalsIgnoreCase("N")
				  || hemisphere.toUpperCase().equalsIgnoreCase("E") ? 1 : -1;
		
		double dd = tryParse(decimalDegrees, 0.0d);
		
		return hemi * dd;
	}
	

}