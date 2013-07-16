package com.berico.clavin.examples;


import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.extractor.coords.RegexCoordinateParsingStrategy;

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

/**
 * Extracts IP Addresses from text.
 */
public class IpAddressParsingStrategy implements RegexCoordinateParsingStrategy<IpAddress> {
	
	/**
	 * Borrowed this IP Address expression from the aweseme @mkyong 
	 * (instead of spending the time to write it ourselves):
	 * http://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
	 */
	public static final String IPADDRESS_PATTERN = "((?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5]))";
	
	/**
	 * Return a compiled REGEX with the IpAddress pattern.
	 * @return Compiled REGEX Pattern
	 */
	@Override
	public Pattern getPattern() {
		
		return Pattern.compile(IPADDRESS_PATTERN);
	}

	/**
	 * Parse the IpAddress from the returned string, returning
	 * a IpAddressOccurrence.
	 * @param ipaddressString String matching the REGEX statement in the document.
	 * @param startPosition The position the occurrence occurred in the document.
	 * @return A IpAddressOccurrence.
	 */
	@Override
	public CoordinateOccurrence<IpAddress> parse(
			String ipaddressString, 
			int startPosition) {
		
		IpAddress ipaddress = new IpAddress(ipaddressString);
		
		return new IpAddressOccurrence(startPosition, ipaddressString, ipaddress);
	}

}
