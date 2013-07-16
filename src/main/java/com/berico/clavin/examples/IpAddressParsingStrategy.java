package com.berico.clavin.examples;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * IpAddressParsingStrategy.java
 * 
 *###################################################################*/

public class IpAddressParsingStrategy implements RegexCoordinateParsingStrategy<IpAddress> {

	private static final Logger logger = LoggerFactory.getLogger(IpAddressParsingStrategy.class);
	
	//public static final String IPADDRESS_PATTERN = "(?<ipaddress>[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
	public static final String IPADDRESS_PATTERN = "(?<ipaddress>([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]))";
	
	/**
	 * Return a compiled REGEX with the IpAddress pattern.
	 * @return Compiled REGEX Pattern
	 */
	@Override
	public Pattern getPattern() {
		
		logger.info("Getting compiled Pattern");
		
		return Pattern.compile(IPADDRESS_PATTERN);
	}

	/**
	 * Parse the IpAddress from the returned REGEX named groups, returning
	 * a IpAddressOccurrence.
	 * @param matchedString String matching the REGEX statement in the document.
	 * @param namedGroups REGEX named capture groups.
	 * @param startPosition The position the occurrence occurred in the document.
	 * @return A IpAddressOccurrence.
	 */
	@Override
	public CoordinateOccurrence<IpAddress> parse(
			String matchedString, 
			Map<String, String> namedGroups, 
			int startPosition) {
		
		logger.info("Parsing ip address.");
		
		String ipaddressString = matchedString;
		
		//String ipaddressString = namedGroups.get("ipaddress");
		
		logger.info("Parsed", ipaddressString);
		
		IpAddress ipaddress = new IpAddress(ipaddressString);
		
		logger.info("Instantiated IpAddress object {}", ipaddress);
		
		return new IpAddressOccurrence(startPosition, matchedString, ipaddress);
	}

}
