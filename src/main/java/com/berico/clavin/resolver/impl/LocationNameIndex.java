package com.berico.clavin.resolver.impl;

import java.util.List;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedLocation;

public interface LocationNameIndex {
	
	List<ResolvedLocation> search(
			LocationOccurrence occurrence, 
			boolean useFuzzy) throws Exception;
	
	List<ResolvedLocation> search(
			LocationOccurrence occurrence, 
			int limit,
			boolean useFuzzy) throws Exception;
	
}
