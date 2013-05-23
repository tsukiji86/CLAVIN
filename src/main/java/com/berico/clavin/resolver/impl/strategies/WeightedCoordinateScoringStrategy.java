package com.berico.clavin.resolver.impl.strategies;

import java.util.Collection;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.CoordinateCandidateSelectionStrategy;

public class WeightedCoordinateScoringStrategy 
	extends GenericWeightedSelectionStrategy<ResolvedCoordinate, Collection<LocationOccurrence>>
	implements CoordinateCandidateSelectionStrategy {
	
	public WeightedCoordinateScoringStrategy() {
		
		super();
	}

	public WeightedCoordinateScoringStrategy(
			Collection<Weigher<ResolvedCoordinate, 
			Collection<LocationOccurrence>>> weighers) {
		
		super(weighers);
	}
	
}
