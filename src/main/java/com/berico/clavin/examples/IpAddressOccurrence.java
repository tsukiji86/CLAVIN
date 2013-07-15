package com.berico.clavin.examples;

import java.io.IOException;

import com.berico.clavin.extractor.coords.BaseCoordinateOccurrence;
import com.berico.clavin.gazetteer.LatLon;
import com.maxmind.geoip2.exception.GeoIp2Exception;

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
 * GeoHashOccurrence.java
 * 
 *###################################################################*/

/**
 * Represents an occurrence of a IpAddress in a document.
 */

public class IpAddressOccurrence extends BaseCoordinateOccurrence<IpAddress>  {
	
	/**
	 * For serialization purposes.
	 */
	public IpAddressOccurrence() { super(); }

	/**
	 * Initialize with the GeoHash value.
	 * @param position Position in document.
	 * @param text Extracted GeoHash text.
	 * @param value The GeoHash.
	 */
	public IpAddressOccurrence(long position, String text, IpAddress value) {
		super(position, text, value);
	}

	/**
	 * Get the coordinate system, in this case, "ipaddress".
	 * @return "ipaddress"
	 */
	@Override
	public String getCoordinateSystem() {
		
		return "ipaddress";
	}

	/**
	 * Convert the coordinate to it's Location representation.
	 * @return LatLon value of the coordinate.
	 */
	@Override
	public LatLon convertToLatLon() throws IOException, GeoIp2Exception {
		
		return this.value.getLatLonValue();
	}


}