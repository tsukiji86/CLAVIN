package com.berico.clavin.extractor.coords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.gazetteer.LatLon;
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
 * BaseRegexPatternParsingStrategy.java
 * 
 *###################################################################*/

/**
 * Simplifies the creation of REGEX-based parsing strategies, reducing the 
 * process to supplying the REGEX pattern and handling the resulting
 * matched REGEX groups.
 */
public abstract class BaseRegexPatternParsingStrategy 
	implements RegexCoordinateParsingStrategy<LatLon> {

	private static final Logger logger = 
			LoggerFactory.getLogger(BaseRegexPatternParsingStrategy.class);
	
	protected Pattern PATTERN = null;
	
	/**
	 * Require deriving classes to supply the Regex pattern.
	 * @return Regex pattern.  
	 */
	protected abstract String getRegexPattern();
	
	/**
	 * Implements the "getPattern" requirement of 
	 * RegexCoordinateParsingStrategy, in this case, lazily
	 * compiling a Regex string from inheriting classes.
	 * @return Compiled Regex statement.
	 */
	@Override
	public Pattern getPattern() {
		
		if (PATTERN == null){
			
			PATTERN = Pattern.compile(this.getRegexPattern());
		}
		
		return PATTERN;
	}
	
	/**
	 * Try parsing the integer, and if it fails, return the default value.
	 * @param value String representation of the integer.
	 * @param defaultValue Default value to use if parsing fails.
	 * @return Hopefully, the parsed integer value.
	 */
	protected static int tryParse(String value, int defaultValue){
		
		if (value == null || value.isEmpty())
			return defaultValue;
		
		int intValue = defaultValue;
		
		try {
			
			intValue = Integer.parseInt(value);
		}
		catch (NumberFormatException nfe){
			
			logger.error("Could not parse integer from string '{}'", value);
		}
		
		return intValue;
	}
	
	/**
	 * Try parsing the double, and if it fails, return the default value.
	 * @param value String representation of the double.
	 * @param defaultValue Default value to use if parsing fails.
	 * @return Hopefully, the parsed double value.
	 */
	protected static double tryParse(String value, double defaultValue){
		
		if (value == null || value.isEmpty())
			return defaultValue;
		
		double doubleValue = defaultValue;
		
		try {
			
			doubleValue = Double.parseDouble(value);
		}
		catch (NumberFormatException nfe){
			
			logger.error("Could not parse double from string '{}'", value);
		}
		
		return doubleValue;
	}
}
