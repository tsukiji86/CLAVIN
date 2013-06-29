package com.berico.clavin.resolver.impl;

import java.util.List;

import com.berico.clavin.extractor.ExtractionContext;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.ResolvedLocation;

/*#####################################################################
 * 
 * CLAVIN (Cartographic Location And Vicinity INdexer)
 * ---------------------------------------------------
 * 
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 * 
 * ====================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * ====================================================================
 * 
 * ResolutionResultsReductionStrategy.java
 * 
 *###################################################################*/

/**
 * Represents a phase in the CLAVIN workflow for reducing (or filtering) results.
 */
public interface ResolutionResultsReductionStrategy {

	/**
	 * Reduce the results into a ResolutionContext.
	 * @param extractionContext Extraction results.
	 * @param locations Best resolved location candidates.
	 * @param coordinates Best resolved coordinate candidates.
	 * @return Results of Resolution.
	 */
	ResolutionContext reduce(
		ExtractionContext extractionContext,
		List<ResolvedLocation> locations, 
		List<ResolvedCoordinate> coordinates);
}