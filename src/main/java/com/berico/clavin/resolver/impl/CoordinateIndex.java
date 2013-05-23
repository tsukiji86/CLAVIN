package com.berico.clavin.resolver.impl;

import java.util.List;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;

public interface CoordinateIndex {
	
	List<ResolvedCoordinate> search(
			CoordinateOccurrence<?> coordinate) throws Exception;
	
	List<ResolvedCoordinate> search(
			CoordinateOccurrence<?> coordinate, int distanceInKm) throws Exception;
	
}
