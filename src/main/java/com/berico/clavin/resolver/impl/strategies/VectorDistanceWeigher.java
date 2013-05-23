package com.berico.clavin.resolver.impl.strategies;

import java.util.Collection;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;

public class VectorDistanceWeigher 
	implements Weigher<ResolvedCoordinate, Collection<LocationOccurrence>> {
	
	public static double DISTANCE_WEIGHT = 2d;
	
	@Override
	public double weigh(
			ResolvedCoordinate item,
			Collection<LocationOccurrence> context) {
		
		return DISTANCE_WEIGHT / item.getVectorFromKnownLocation().getMagnitude();
	}
}
