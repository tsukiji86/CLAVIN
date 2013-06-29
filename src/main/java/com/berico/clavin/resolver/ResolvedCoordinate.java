package com.berico.clavin.resolver;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.Place;

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
 * ResolvedCoordinate.java
 * 
 *###################################################################*/

/**
 * Represents the results of the Coordinate Resolution process, that is,
 * converting Lat/Lon (or some other coordinate system) to a resolved Place
 * object.
 */
public class ResolvedCoordinate {
	
	protected Vector vectorFromKnownLocation;
	
	protected CoordinateOccurrence<?> occurrence;
	
	protected Place knownLocation;

	// TODO: Add confidence as a function of the normalized result set.
	
	/**
	 * For serialization purposes.
	 */
	public ResolvedCoordinate(){}
	
	/**
	 * Initialize the ResolvedCoordinate.
	 * @param occurrence Coordinate found in text
	 * @param knownLocation Most probable Place the coordinate is associated with.
	 * @param vectorFromKnownLocation Direction and Distance from the center of 
	 * the Place this coordinate was resolved to.
	 */
	public ResolvedCoordinate(
			CoordinateOccurrence<?> occurrence, 
			Place knownLocation,
			Vector vectorFromKnownLocation) {
		
		this.vectorFromKnownLocation = vectorFromKnownLocation;
		this.occurrence = occurrence;
		this.knownLocation = knownLocation;
	}
	
	/**
	 * Get the distance and direction of the coordinate from the resolved Place.
	 * @return Vector from the Place's center.
	 */
	public Vector getVectorFromKnownLocation() {
		return vectorFromKnownLocation;
	}

	/**
	 * Get the coordinates occurrence in text.
	 * @return Coordinate Occurrence.
	 */
	public CoordinateOccurrence<?> getOccurrence() {
		return occurrence;
	}

	/**
	 * Get the place this coordinate was resolved to.
	 * @return Place coordinate was resolved to.
	 */
	public Place getKnownLocation() {
		return knownLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((knownLocation == null) ? 0 : knownLocation.hashCode());
		result = prime * result
				+ ((occurrence == null) ? 0 : occurrence.hashCode());
		result = prime
				* result
				+ ((vectorFromKnownLocation == null) ? 0
						: vectorFromKnownLocation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResolvedCoordinate other = (ResolvedCoordinate) obj;
		if (knownLocation == null) {
			if (other.knownLocation != null)
				return false;
		} else if (!knownLocation.equals(other.knownLocation))
			return false;
		if (occurrence == null) {
			if (other.occurrence != null)
				return false;
		} else if (!occurrence.equals(other.occurrence))
			return false;
		if (vectorFromKnownLocation == null) {
			if (other.vectorFromKnownLocation != null)
				return false;
		} else if (!vectorFromKnownLocation
				.equals(other.vectorFromKnownLocation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Resolved: ")
		  .append('"')
		  .append(occurrence.getExtractedText())
		  .append('"')
		  .append(" as ")
		  .append(vectorFromKnownLocation)
		  .append(" of ")
		  .append(knownLocation.getName())
		  .append(", [")
		  .append(knownLocation.getPrimaryCountryCode())
		  .append("]");
		  
		return sb.toString();
	}
	
}
