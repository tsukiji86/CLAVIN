package com.berico.clavin.extractor.coords;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
 * DdHemiLetterPatternParsingStrategyTest.java
 * 
 *###################################################################*/

public class DdHemiLetterPatternParsingStrategyTest 
		extends BaseRegexParsingStrategyTest {

	DdHemiLetterPatternParsingStrategy strategy = new DdHemiLetterPatternParsingStrategy();
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void pattern_matches_decimal_degree_coordinate_with_letter_for_hemisphere_in_text() {
		
		String text = "I went to this great place (40.446195N 79.948862W) last week.";
		
		this.assertPatternMatches(strategy, text, "40.446195N 79.948862W");
	}

	@Test
	public void strategy_correctly_parses_matched_decimal_degrees_with_letter_for_hemispheres_string(){
		
		String testCoordinate = "40.446195N 79.948862W";
		int position = 42;
		
		CoordinateOccurrence<LatLon> coordinate = 
			strategy.parse(testCoordinate, position);
		
		assertEquals(testCoordinate, coordinate.getExtractedText());
		assertEquals(position, coordinate.getPosition());
		assertEquals(40.446195, coordinate.getValue().getLatitude(), 0.0001);
		assertEquals(-79.948862, coordinate.getValue().getLongitude(), 0.0001);
	}
	
	@Test
	public void strategy_correctly_parses_matched_decimal_degrees_with_letter_for_hemispheres__swap_negative__string(){
		
		String testCoordinate = "40.446195S 79.948862E";
		int position = 42;
		
		CoordinateOccurrence<LatLon> coordinate = 
			strategy.parse(testCoordinate, position);
		
		assertEquals(testCoordinate, coordinate.getExtractedText());
		assertEquals(position, coordinate.getPosition());
		assertEquals(-40.446195, coordinate.getValue().getLatitude(), 0.0001);
		assertEquals(79.948862, coordinate.getValue().getLongitude(), 0.0001);
	}
	
}
