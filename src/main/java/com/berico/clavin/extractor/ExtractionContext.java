package com.berico.clavin.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
* ExtractionContext.java
*
*###################################################################*/

/**
 * Represents the combined results of both Location and Coordinate extraction.
 */
public class ExtractionContext {

	/**
	 * Text in which extractions occurred.
	 */
	protected String originalText;
	
	/**
	 * Set of locations that were found in the document.
	 */
	protected ArrayList<LocationOccurrence> locations =
			new ArrayList<LocationOccurrence>();
	
	/**
	 * Set of coordinates that were found in the document.
	 */
	protected ArrayList<CoordinateOccurrence<?>> coordinates =
			new ArrayList<CoordinateOccurrence<?>>();

	/**
	 * For serialization purposes; please use the parameterized constructor.
	 */
	public ExtractionContext(){}
	
	/**
	 * Initialize the context with the original document and all locations and
	 * coordinates found in the text.
	 * @param originalText Document Text
	 * @param locations Locations found in document.
	 * @param coordinates Coordinates found in document.
	 */
	public ExtractionContext(
			String originalText,
			Collection<LocationOccurrence> locations,
			Collection<CoordinateOccurrence<?>> coordinates) {
		
		this.originalText = originalText;
		
		if (locations != null){
			this.locations.addAll(locations);
		}
		
		if (coordinates != null){
			this.coordinates.addAll(coordinates);
		}
	}
	
	/**
	 * Get the original text.
	 * @return Document text.
	 */
	public String getOriginalText() {
		
		return originalText;
	}

	/**
	 * An unmodifiable list of locations found in the document.
	 * @return Locations found.
	 */
	public List<LocationOccurrence> getLocations() {
		
		return Collections.unmodifiableList(locations);
	}

	/**
	 * An unmodifiable list of coodinates found in the document.
	 * @return Coordinates found.
	 */
	public List<CoordinateOccurrence<?>> getCoordinates() {
		
		return Collections.unmodifiableList(coordinates);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result
				+ ((locations == null) ? 0 : locations.hashCode());
		result = prime * result
				+ ((originalText == null) ? 0 : originalText.hashCode());
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
		ExtractionContext other = (ExtractionContext) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		if (originalText == null) {
			if (other.originalText != null)
				return false;
		} else if (!originalText.equals(other.originalText))
			return false;
		return true;
	}	
	
	@Override
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("ExtractionContext:\n");
		sb.append("\tLocations:  ").append(this.locations);
		sb.append("\tCoordinates:  ").append(this.coordinates);
		
		return sb.toString();
	}
}
