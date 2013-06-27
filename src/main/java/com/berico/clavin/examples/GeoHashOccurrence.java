package com.berico.clavin.examples;

import com.berico.clavin.extractor.coords.BaseCoordinateOccurrence;
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
 * GeoHashOccurrence.java
 * 
 *###################################################################*/

/**
 * Represents an occurrence of a GeoHash ({@linkplain http://geohash.org/}) in a document.
 */
public class GeoHashOccurrence extends BaseCoordinateOccurrence<GeoHash>  {
	
	/**
	 * For serialization purposes.
	 */
	public GeoHashOccurrence() { super(); }

	/**
	 * Initialize with the GeoHash value.
	 * @param position Position in document.
	 * @param text Extracted GeoHash text.
	 * @param value The GeoHash.
	 */
	public GeoHashOccurrence(long position, String text, GeoHash value) {
		super(position, text, value);
	}

	/**
	 * Get the coordinate system, in this case, "geohash".
	 * @return "geohash"
	 */
	@Override
	public String getCoordinateSystem() {
		
		return "geohash";
	}

	/**
	 * Convert the coordinate to it's LatLon representation.
	 * @return LatLon value of the coordinate.
	 */
	@Override
	public LatLon convertToLatLon() {
		
		return this.value.getLatLonValue();
	}

}
