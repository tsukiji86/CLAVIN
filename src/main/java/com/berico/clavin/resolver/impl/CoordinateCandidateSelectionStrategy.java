package com.berico.clavin.resolver.impl;

import java.util.Collection;
import java.util.List;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;

public interface CoordinateCandidateSelectionStrategy {
	
	List<ResolvedCoordinate> select(
		List< List<ResolvedCoordinate> > coordinates,
		Collection<LocationOccurrence> cooccurringLocations) throws Exception;
	
}