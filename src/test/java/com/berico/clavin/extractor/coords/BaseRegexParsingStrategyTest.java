package com.berico.clavin.extractor.coords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.code.regexp.Pattern;
import com.google.code.regexp.Matcher;

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
 * BaseRegexParsingStrategyTest.java
 * 
 *###################################################################*/

public class BaseRegexParsingStrategyTest {

	public void assertPatternMatches(
		RegexCoordinateParsingStrategy<?> strategy, 
		String text, 
		String expectedMatch){
		
		Pattern p = strategy.getPattern();
		
		Matcher m = p.matcher(text);
		
		assertTrue(m.find());
		
		assertEquals(expectedMatch, m.group());
	}
	
}
