package com.berico.clavin.resolver;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.GeoName;

public class ResolvedCoordinate {
	
	private static final ConfidenceComparator 
		confidenceComparator = new ConfidenceComparator();
	
	protected TreeMap<Float, GeoName> nearbyLocations = 
			new TreeMap<Float, GeoName>(confidenceComparator);
	
	protected CoordinateOccurrence<?> coordinateOccurrence;
	
	public ResolvedCoordinate(){}
	
	public ResolvedCoordinate(
			Map<Float, GeoName> nearbyLocations,
			CoordinateOccurrence<?> coordinateOccurrence) {
		
		this.coordinateOccurrence = coordinateOccurrence;
		setNearbyLocations(nearbyLocations);
	}

	public Map<Float, GeoName> getNearbyLocations() {
		return Collections.unmodifiableMap(nearbyLocations);
	}
	
	protected void setNearbyLocations(Map<Float, GeoName> nearbyLocations) {
		
		assert nearbyLocations != null;
		
		this.nearbyLocations.putAll(nearbyLocations);
	}
	
	public CoordinateOccurrence<?> getCoordinateOccurrence() {
		return coordinateOccurrence;
	}
	
	
	/**
	 * Comparator for Ordering the nearby locations set based on confidence.
	 */
	public static class ConfidenceComparator implements Comparator<Float> {

		@Override
		public int compare(Float conf1, Float conf2) {
			
			return - Float.compare(conf1, conf2);
		}
	}
}
