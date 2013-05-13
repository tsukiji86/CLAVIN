package com.berico.clavin.extractor;

import com.berico.clavin.extractor.coords.LatLonPair;

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
* CoordinateOccurrence.java
*
*###################################################################*/


/**
 * Defines the necessary information the LocationResolver
 * will need to perform reverse resolution on a coordinate.
 *
 * @param <T> Object representing the coordinate.
 */
public interface CoordinateOccurrence<T> {
	
	/**
	 * Get the starting position of the extracted coordinate
	 * in the document. 
	 * @return Position in document.
	 */
	long getPosition();
	
	/**
	 * Get the text found in the document representing the 
	 * extracted coordinate.
	 * @return Extracted text
	 */
	String getExtractedText();
	
	/**
	 * Get the name of the coordinate system.  This may be used
	 * by the indexer to determine the strategy for indexing
	 * or lookup.
	 * @return Name of Coordinate System
	 */
	String getCoordinateSystem();
	
	/**
	 * Get the value of the Coordinate.
	 * @return The value of the Coordinate in its native form.
	 */
	T getValue();
	
	/**
	 * The one imposition we place on a coordinate occurrence
	 * is that is must be converted to a Lat/Lon Pair.
	 * @return Lat/Lon representation of the location.
	 */
	LatLonPair convertToLatLon();
}