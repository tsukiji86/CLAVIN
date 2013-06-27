package com.berico.clavin.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.berico.clavin.extractor.ExtractionContext;

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
 * ResolutionContext.java
 * 
 *###################################################################*/

/**
 * Represents the combined results of resolution (coordinates and plain-named
 * locations disambiguated).
 */
public class ResolutionContext {

	protected ArrayList<ResolvedLocation> locations = 
			new ArrayList<ResolvedLocation>();
	
	protected ArrayList<ResolvedCoordinate> coordinates = 
			new ArrayList<ResolvedCoordinate>();
	
	protected ExtractionContext extractionContext;
	
	/**
	 * For serialization purposes.
	 */
	public ResolutionContext(){}
	
	/**
	 * Instantiate the Resolution Context with the results of the resolution
	 * process.
	 * @param locations Collection of Resolved Locations
	 * @param coordinates Collection of Resolved Coordinates
	 * @param extractionContext Context of the previous process (Extraction)
	 */
	public ResolutionContext(
			Collection<ResolvedLocation> locations,
			Collection<ResolvedCoordinate> coordinates,
			ExtractionContext extractionContext) {
		
		this.setLocations(locations);
		this.setCoordinates(coordinates);
		this.extractionContext = extractionContext;
	}

	/**
	 * Set the Resolved Locations
	 * @param locations Collection of Resolved Locations
	 */
	protected void setLocations(Collection<ResolvedLocation> locations){
		
		assert locations != null;
		
		this.locations.addAll(locations);
	}
	
	/**
	 * Get the Locations Resolved from the Extraction Context
	 * @return Collection of Resolved Locations.
	 */
	public List<ResolvedLocation> getLocations() {
		return locations;
	}

	/**
	 * Set the Resolved Coordinates
	 * @param coordinates Collection of Resolved Coordinates
	 */
	protected void setCoordinates(Collection<ResolvedCoordinate> coordinates){
		
		assert coordinates != null;
		
		this.coordinates.addAll(coordinates);
	}
	
	/**
	 * Get the Collection of Coordinates that were resolved to Places.
	 * @return Collection of Resolved Coordinates.
	 */
	public List<ResolvedCoordinate> getCoordinates() {
		return coordinates;
	}

	/**
	 * Set the extraction context.
	 * @param context Information regarding the previous step, the extraction
	 * process.
	 */
	public void setExtractionContext(ExtractionContext context){
		
		this.extractionContext = context;
	}
	
	/**
	 * Get the extraction context.
	 * @return Information about the previous step in the workflow process.
	 */
	public ExtractionContext getExtractionContext() {
		return extractionContext;
	}
}
