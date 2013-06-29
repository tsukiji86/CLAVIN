package com.berico.clavin.resolver.impl.strategies;

import java.util.List;

import com.berico.clavin.extractor.ExtractionContext;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.impl.ResolutionResultsReductionStrategy;

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
 * IdentityReductionStrategy.java
 * 
 *###################################################################*/

/**
 * Whatever goes in comes out.  This is the default strategy and assumes you want to
 * keep everything in the result set and don't need to perform any aggregations.
 */
public class IdentityReductionStrategy implements ResolutionResultsReductionStrategy {

	/**
	 * Takes the input from previous processes and returns the ResolutionContext.
	 * @param extractionContext The results from extraction.
	 * @param locations resolved locations
	 * @param coordinates resolved coordinates
	 * @return ResolutionContext (results from the process).
	 */
	@Override
	public ResolutionContext reduce(ExtractionContext extractionContext,
			List<ResolvedLocation> locations,
			List<ResolvedCoordinate> coordinates) {	
		
		return new ResolutionContext(locations, coordinates, extractionContext);
	}

}
