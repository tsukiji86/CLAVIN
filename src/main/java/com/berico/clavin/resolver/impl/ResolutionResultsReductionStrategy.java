package com.berico.clavin.resolver.impl;

import java.util.List;

import com.berico.clavin.extractor.ExtractionContext;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.ResolvedLocation;

public interface ResolutionResultsReductionStrategy {

	ResolutionContext reduce(
		ExtractionContext extractionContext,
		List<ResolvedLocation> locations, 
		List<ResolvedCoordinate> coordinates);
}