package com.berico.clavin.extractor.coords;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.berico.clavin.extractor.CoordinateOccurrence;

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
 * RegexCoordinateExtractorTest.java
 * 
 *###################################################################*/

public class RegexCoordinateExtractorTest {

	public static final String TEST_TEXT = "I went to the school near " +
			"40.446195, 79.948862 yesterday and found a book on the climate of " +
			"-40.446195, 79.948862.  Equally interesting was the coffee in NoTown, " +
			"-23.399437,-52.090904, which was made from beans from 40:26:46.302N 079:56:55.903W." +
			"Perhaps I'll venture to 40°26'N 079°58'W next week for some awesome brews!";
	
	@Test
	public void extracts_coordinates_using_multiple_strategies(){
		
		ArrayList<RegexCoordinateParsingStrategy<?>> strategies = 
				new ArrayList<RegexCoordinateParsingStrategy<?>>();
		
		strategies.add(new DdPatternParsingStrategy());
		strategies.add(new DmsPatternParsingStrategy());
		
		RegexCoordinateExtractor extractor = new RegexCoordinateExtractor(strategies);
		
		List<CoordinateOccurrence<?>> actual = extractor.extractCoordinates(TEST_TEXT);
		
		assertEquals(actual.size(), 5);
	}

}
