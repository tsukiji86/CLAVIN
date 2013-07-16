package com.berico.clavin.extractor.coords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.extractor.CoordinateExtractor;
import com.berico.clavin.extractor.CoordinateOccurrence;

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
 * RegexCoordinateExtractor.java
 * 
 *###################################################################*/

/**
 * Manages the extractions of coordinates using a set of REGEX-based
 * extraction strategies.
 */
public class RegexCoordinateExtractor implements CoordinateExtractor {
	
	private static final Logger logger = LoggerFactory.getLogger(RegexCoordinateExtractor.class);
	
	ArrayList<RegexCoordinateParsingStrategy<?>> strategies 
		= new ArrayList<RegexCoordinateParsingStrategy<?>>();

	/**
	 * Instantiate the coordinate extractor with the set of coordinate
	 * extraction strategies.
	 * @param strategies REGEX-based coordinate parsers.
	 */
	public RegexCoordinateExtractor(
			Collection<RegexCoordinateParsingStrategy<?>> strategies) {
		
		assert strategies != null;
		
		this.strategies.addAll(strategies);
	}
	
	/**
	 * Extract coordinates from text.
	 * @param text Text to perform the extraction on.
	 * @return The set of coordinate occurrences found in the text.
	 */
	@Override
	public List<CoordinateOccurrence<?>> extractCoordinates(String text) {
		
		ArrayList<CoordinateOccurrence<?>> occurrences = 
			new ArrayList<CoordinateOccurrence<?>>();
		
		// Iterate through each strategy
		for (RegexCoordinateParsingStrategy<?> strategy : this.strategies){
			
			Pattern pattern = strategy.getPattern();
			
			Matcher matcher = pattern.matcher(text);
			
			// If a match is found, iterate through the matches
			while (matcher.find()){
				
				logger.info("Found another match.");
				
				// Extracting the coordinate occurrence
				CoordinateOccurrence<?> occurrence = 
					strategy.parse(matcher.group(), matcher.start());
				
				// and adding the results.
				occurrences.add(occurrence);
			}
		}
		
		return occurrences;
	}
}
