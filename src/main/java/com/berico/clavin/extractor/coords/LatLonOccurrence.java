package com.berico.clavin.extractor.coords;

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
 * LatLonOccurrence.java
 * 
 *###################################################################*/

/**
 * Represents a CoordinateOccurrence in the form of a Latitude and Longitude (LatLon).
 */
public class LatLonOccurrence extends BaseCoordinateOccurrence<LatLon> {
	
	/**
	 * For serialization purposes.
	 */
	public LatLonOccurrence() { super(); }

	/**
	 * Initialize with context.
	 * @param position Position in document.
	 * @param text Extracted coordinate text.
	 * @param value LatLon value of the coordinate.
	 */
	public LatLonOccurrence(long position, String text, LatLon value) { 
		super(position, text, value);
	}

	/**
	 * Get the underlying coordinate system.
	 * @return Coordinate system.
	 */
	@Override
	public String getCoordinateSystem() {
		
		return "LatLon";
	}

	/**
	 * Convert the value to it's LatLon representation (hey, it's already a LatLon!).
	 * @return LatLon value of the coordinate.
	 */
	@Override
	public LatLon convertToLatLon() {
		
		return value;
	}
}
