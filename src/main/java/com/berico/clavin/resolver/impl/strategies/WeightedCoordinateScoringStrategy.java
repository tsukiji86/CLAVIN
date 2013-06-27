package com.berico.clavin.resolver.impl.strategies;

import java.util.Collection;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.CoordinateCandidateSelectionStrategy;

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
 * WeightedCoordinateScoringStrategy.java
 * 
 *###################################################################*/

/**
 * Essentially just a container for weighers in the Coordinate Selection Strategy.
 */
public class WeightedCoordinateScoringStrategy 
	extends GenericWeightedSelectionStrategy<ResolvedCoordinate, Collection<LocationOccurrence>>
	implements CoordinateCandidateSelectionStrategy {
	
	/**
	 * Initialize the Composite Strategy.
	 */
	public WeightedCoordinateScoringStrategy() {
		
		super();
	}

	/**
	 * Initialize the Composite Strategy with its aggregate strategies.
	 * @param weighers Collection of weighers to use for selections.
	 */
	public WeightedCoordinateScoringStrategy(
			Collection<Weigher<ResolvedCoordinate, 
			Collection<LocationOccurrence>>> weighers) {
		
		super(weighers);
	}
	
}
