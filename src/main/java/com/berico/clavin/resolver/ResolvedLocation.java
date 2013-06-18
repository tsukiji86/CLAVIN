package com.berico.clavin.resolver;

import com.berico.clavin.extractor.LocationOccurrence;
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
 * ResolvedLocation.java
 * 
 *###################################################################*/

/**
 * Object produced by resolving a location name against gazetteer
 * records.
 * 
 * Encapsulates a {@link Place} object representing the best match
 * between a given location name and gazetteer record, along with some
 * information about the geographic entity resolution process.
 *
 */
public class ResolvedLocation {
	
	// geographic entity resolved from location name
	protected Place place;
	
	// original location name extracted from text
	protected LocationOccurrence location;
	
	// name from gazetteer record that the inputName was matched against
	protected String matchedName;
	
	// whether fuzzy matching was used
	protected boolean fuzzy;
	
	// confidence score for resolution
	protected float confidence;
	
	/**
	 * For serialization purposes, don't use directly.
	 */
	public ResolvedLocation(){}
	
	/**
	 * Represents a {@link ResolvedLocation} mapping to an extracted plain-nammed location
	 * found in a document.
	 * 
	 * @param matchedName Name of the location that was matched by the extracted text.
	 * @param place The resolved place.
	 * @param location Context of the extraction (text and position)
	 * @param fuzzy Whether fuzzy matching was applied.
	 * @param confidence Confidence of the match.
	 */
	public ResolvedLocation(
			String matchedName,
			Place place, 
			LocationOccurrence location, 
			boolean fuzzy,
			float confidence){
		
		this.matchedName = matchedName;
		this.place = place;
		this.location = location;
		this.fuzzy = fuzzy;
		this.confidence = confidence;
	}
	
	/**
	 * Get the Place entry for this resolved location.
	 * @return Place entry
	 */
	public Place getPlace() {
		return place;
	}

	/**
	 * Get the context of the extracted location.
	 * @return Occurrence of the extracted location in text.
	 */
	public LocationOccurrence getLocation() {
		return location;
	}

	/**
	 * Get the name matching the extracted location found by the resolver.
	 * @return Name resolved used to match this entry.
	 */
	public String getMatchedName() {
		return matchedName;
	}

	/**
	 * Was fuzzy matching used to select this location?
	 * @return true if fuzzy matching was used.
	 */
	public boolean isFuzzy() {
		return fuzzy;
	}

	/**
	 * Get the confidence of the match.
	 * @return Confidence of the match between the matched name and the text occurring
	 * in the document.
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Tests equivalence between {@link ResolvedLocation} objects.
	 * 
	 * @param obj	the other object being compared against
	 */
	@Override
	public boolean equals(Object obj) {
	    if (obj == this) return true;
	    if (obj == null) return false;
	    
	    // only a ResolvedLocation can equal a ResolvedLocation
	    if (this.getClass() != obj.getClass()) return false;
	    
	    // cast the other object into a ResolvedLocation, now that we
	    // know that it is one
	    ResolvedLocation other = (ResolvedLocation)obj;
	    
	    // as long as the Place IDs are the same, we'll treat these
	    // ResolvedLocations as equal since they point to the same
	    // geographic entity (even if the circumstances of the entity
	    // resolution process differed)
	    return (this.place.getId() == other.place.getId());
	}
	
	private static final char dblquote = '"';
	
	/**
	 * For pretty-printing.
	 * 
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Resolved ")
		  // Text
		  .append(dblquote).append(location.getText()).append(dblquote).append(" ")
		  .append("as")
		  // Matched Name
		  .append(dblquote).append(matchedName).append(dblquote).append(" ")
		  // Place
		  .append("{ ").append(place).append("}, ")
		  // Position in Document
		  .append("position: ").append(location.getPosition()).append(", ")
		  // Confidence
		  .append("confidence: ").append(confidence).append(", ")
		  // Fuzziness
		  .append("fuzzy: ").append(fuzzy);
		
		return sb.toString();
	}
}
