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
 * DmsPattern1ParsingStrategyTest.java
 * 
 *###################################################################*/

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.berico.clavin.extractor.CoordinateOccurrence;

public class DmsPatternParsingStrategyTest 
		extends BaseRegexParsingStrategyTest {
	
	DmsPatternParsingStrategy strategy = new DmsPatternParsingStrategy();
	
	@Test
	public void pattern_matches_deg_min_sec_subsec_coordinate_with_colon_separators_in_text() {
		
		String text = "I went to this great place (40:26:46.302N 079:56:55.903W) last week.";
		
		this.assertPatternMatches(strategy, text, "40:26:46.302N 079:56:55.903W");
	}

	@Test
	public void pattern_matches_deg_min_sec_coordinate_with_colon_separators_in_text() {
		
		String text = "I went to this great place (40:26:46N 079:56:55W) last week.";
		
		this.assertPatternMatches(strategy, text, "40:26:46N 079:56:55W");
	}
	
	@Test
	public void pattern_matches_deg_min_coordinate_with_colon_separators_in_text() {
		
		String text = "I went to this great place (40:26N 079:56W) last week.";
		
		this.assertPatternMatches(strategy, text, "40:26N 079:56W");
	}
	
	@Test
	public void pattern_matches_deg_min_sec_coordinate_with_differing_separators1_in_text() {
		
		String text = "I went to this great place (40°26′47″N 079°58′36″W) last week.";
		
		this.assertPatternMatches(strategy, text, "40°26′47″N 079°58′36″W");
	}
	
	@Test
	public void pattern_matches_deg_min_coordinate_with_differing_separators1_in_text() {
		
		String text = "I went to this great place (40°26′N 079°58′W) last week.";
		
		this.assertPatternMatches(strategy, text, "40°26′N 079°58′W");
	}
	
	@Test
	public void pattern_matches_deg_min_sec_coordinate_with_differing_separators2_in_text() {
		
		String text = "I went to this great place (40°26'47\"N 079°58'36\"W) last week.";
		
		this.assertPatternMatches(strategy, text, "40°26'47\"N 079°58'36\"W");
	}
	
	@Test
	public void pattern_matches_deg_min_coordinate_with_differing_separators2_in_text() {
		
		String text = "I went to this great place (40°26'N 079°58'W) last week.";
		
		this.assertPatternMatches(strategy, text, "40°26'N 079°58'W");
	}
	
	@Test
	public void pattern_matches_deg_min_sec_coordinate_with_differing_separators3_in_text() {
		
		String text = "I went to this great place (40d 26′ 47″ N 079d 58′ 36″ W) last week.";
		
		this.assertPatternMatches(strategy, text, "40d 26′ 47″ N 079d 58′ 36″ W");
	}
	
	@Test
	public void pattern_matches_deg_min_coordinate_with_differing_separators3_in_text() {
		
		String text = "I went to this great place (40d 26′ N 079d 58′ W) last week.";
		
		this.assertPatternMatches(strategy, text, "40d 26′ N 079d 58′ W");
	}
	
	@Test
	public void pattern_matches_deg_min_sec_coordinate_with_differing_separators4_in_text() {
		
		String text = "I went to this great place (40d 26' 47\" N 079d 58' 36\" W last week.";
		
		this.assertPatternMatches(strategy, text, "40d 26' 47\" N 079d 58' 36\" W");
	}
	
	@Test
	public void pattern_matches_deg_min_coordinate_with_differing_separators4_in_text() {
		
		String text = "I went to this great place (40d 26' N 079d 58' W) last week.";
		
		this.assertPatternMatches(strategy, text, "40d 26' N 079d 58' W");
	}
	
	@Test
	public void strategy_correctly_parses_matched_deg_min_sec_subsec_string(){
		
		String testCoordinate = "40:26:46.302N 079:56:55.903W";
		int position = 42;
		
		HashMap<String, String> namedGroups = new HashMap<String, String>();
		
		namedGroups.put("latdeg",  "40");
		namedGroups.put("latmin",  "26");
		namedGroups.put("latsec",  "46.302");
		namedGroups.put("lathemi", "N");
		namedGroups.put("londeg",  "079");
		namedGroups.put("lonmin",  "56");
		namedGroups.put("lonsec",  "55.903");
		namedGroups.put("lonhemi", "W");
		
		CoordinateOccurrence<LatLonPair> coordinate = 
			strategy.parse(testCoordinate, namedGroups, position);
		
		assertEquals(testCoordinate, coordinate.getExtractedText());
		assertEquals(position, coordinate.getPosition());
		assertEquals(40.446195, coordinate.getValue().getLatitude(), 0.0001);
		assertEquals(-79.948862, coordinate.getValue().getLongitude(), 0.0001);
	}
	
	@Test
	public void strategy_correctly_parses_matched_deg_min_sec_string(){
		
		String testCoordinate = "40:26:46N 079:56:55W";
		int position = 42;
		
		HashMap<String, String> namedGroups = new HashMap<String, String>();
		
		namedGroups.put("latdeg",  "40");
		namedGroups.put("latmin",  "26");
		namedGroups.put("latsec",  "46");
		namedGroups.put("lathemi", "N");
		namedGroups.put("londeg",  "079");
		namedGroups.put("lonmin",  "56");
		namedGroups.put("lonsec",  "55");
		namedGroups.put("lonhemi", "W");
		
		CoordinateOccurrence<LatLonPair> coordinate = 
			strategy.parse(testCoordinate, namedGroups, position);
		
		assertEquals(testCoordinate, coordinate.getExtractedText());
		assertEquals(position, coordinate.getPosition());
		assertEquals(40.4461111, coordinate.getValue().getLatitude(), 0.0001);
		assertEquals(-79.9486111, coordinate.getValue().getLongitude(), 0.0001);
	}
	
	@Test
	public void strategy_correctly_parses_matched_deg_min_string(){
		
		String testCoordinate = "40:26N 079:56W";
		int position = 42;
		
		HashMap<String, String> namedGroups = new HashMap<String, String>();
		
		namedGroups.put("latdeg",  "40");
		namedGroups.put("latmin",  "26");
		namedGroups.put("lathemi", "N");
		namedGroups.put("londeg",  "079");
		namedGroups.put("lonmin",  "56");
		namedGroups.put("lonhemi", "W");
		
		CoordinateOccurrence<LatLonPair> coordinate = 
			strategy.parse(testCoordinate, namedGroups, position);
		
		assertEquals(testCoordinate, coordinate.getExtractedText());
		assertEquals(position, coordinate.getPosition());
		assertEquals(40.4333333, coordinate.getValue().getLatitude(), 0.0001);
		assertEquals(-79.933333, coordinate.getValue().getLongitude(), 0.0001);
	}
}
