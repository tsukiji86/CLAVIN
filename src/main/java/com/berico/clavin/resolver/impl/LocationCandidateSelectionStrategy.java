package com.berico.clavin.resolver.impl;

import java.util.Collection;
import java.util.List;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.resolver.ResolvedLocation;

public interface LocationCandidateSelectionStrategy {
	
	List<ResolvedLocation> select(
		List<List<ResolvedLocation>> allPossibilities,
		Collection<CoordinateOccurrence<?>> cooccurringCoordinates) throws Exception;
}
