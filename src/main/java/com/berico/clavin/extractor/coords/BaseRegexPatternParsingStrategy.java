package com.berico.clavin.extractor.coords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.regexp.Pattern;

public abstract class BaseRegexPatternParsingStrategy 
	implements RegexCoordinateParsingStrategy<LatLonPair> {

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
