package com.berico.clavin.resolver;

import java.util.ArrayList;
import java.util.Collection;

import com.berico.clavin.extractor.ExtractionContext;

public class ResolutionContext {

	protected ArrayList<ResolvedLocation> locations = 
			new ArrayList<ResolvedLocation>();
	
	protected ArrayList<ResolvedCoordinate> coordinates = 
			new ArrayList<ResolvedCoordinate>();
	
	protected ExtractionContext extractionContext;
	
	public ResolutionContext(){}
	
	public ResolutionContext(
			Collection<ResolvedLocation> locations,
			Collection<ResolvedCoordinate> coordinates,
			ExtractionContext extractionContext) {
		
		this.setLocations(locations);
		this.setCoordinates(coordinates);
		this.extractionContext = extractionContext;
	}

	protected void setLocations(Collection<ResolvedLocation> locations){
		
		assert locations != null;
		
		this.locations.addAll(locations);
	}
	
	public Collection<ResolvedLocation> getLocations() {
		return locations;
	}

	protected void setCoordinates(Collection<ResolvedCoordinate> coordinates){
		
		assert coordinates != null;
		
		this.coordinates.addAll(coordinates);
	}
	
	public Collection<ResolvedCoordinate> getCoordinates() {
		return coordinates;
	}

	public ExtractionContext getExtractionContext() {
		return extractionContext;
	}
}
