package com.berico.clavin.examples;

import java.util.Map;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.extractor.coords.RegexCoordinateParsingStrategy;
import com.google.code.regexp.Pattern;

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
 * GeoHashParsingStrategy.java
 * 
 *###################################################################*/

/**
 * Demonstrates how to use the RegexCoordinateParsingStrategy to extract
 * GeoHashes in text (with the form "geo:u4pruydqqvjs") and convert
 * them to a CoordinateOccurrence.
 */
public class GeoHashParsingStrategy implements RegexCoordinateParsingStrategy<GeoHash> {

	public static final String GEOHASH_PATTERN = "(geo[:](?<hash>\\w*))";
	
	/**
	 * Return a compiled REGEX with the GeoHash pattern.
	 * @return Compiled REGEX Pattern
	 */
	@Override
	public Pattern getPattern() {
		
		return Pattern.compile(GEOHASH_PATTERN);
	}

	/**
	 * Parse the GeoHash from the returned REGEX named groups, returning
	 * a GeoHashOccurrence.
	 * @param matchedString String matching the REGEX statement in the document.
	 * @param namedGroups REGEX named capture groups.
	 * @param startPosition The position the occurrence occurred in the document.
	 * @return A GeoHashOccurrence.
	 */
	@Override
	public CoordinateOccurrence<GeoHash> parse(
			String matchedString, 
			Map<String, String> namedGroups, 
			int startPosition) {
		
		String geohashString = namedGroups.get("hash");
		
		GeoHash geohash = new GeoHash(geohashString);
		
		return new GeoHashOccurrence(startPosition, matchedString, geohash);
	}

}
