package com.berico.clavin.resolver;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.GeoName;

public class ResolvedCoordinate {

	protected Vector vectorFromKnownLocation;
	
	protected CoordinateOccurrence<?> occurrence;
	
	protected GeoName knownLocation;

	public ResolvedCoordinate(){}
	
	public ResolvedCoordinate(
			CoordinateOccurrence<?> occurrence, 
			GeoName knownLocation,
			Vector vectorFromKnownLocation) {
		
		this.vectorFromKnownLocation = vectorFromKnownLocation;
		this.occurrence = occurrence;
		this.knownLocation = knownLocation;
	}
	
	public Vector getVectorFromKnownLocation() {
		return vectorFromKnownLocation;
	}

	public CoordinateOccurrence<?> getOccurrence() {
		return occurrence;
	}

	public GeoName getKnownLocation() {
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
		  .append(knownLocation.name)
		  .append(", [")
		  .append(knownLocation.primaryCountryCode)
		  .append("]");
		  
		return sb.toString();
	}

	
	
	
}
