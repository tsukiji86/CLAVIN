package com.berico.clavin.resolver.impl.strategies.coordinates;

import java.util.Collection;

import com.berico.clavin.Options;
import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.strategies.Weigher;

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
* VectorDistanceWeigher.java
*
*###################################################################*/

/**
 * Improve the resolution rank of a ResolvedCoordinate by it's distance
 * from the coordinate found in the document.
 */
public class VectorDistanceWeigher 
	implements Weigher<ResolvedCoordinate, Collection<LocationOccurrence>> {
	
	// Arbitrary weight reduced by greater vector magnitudes (distance from coordinate).
	public static double DISTANCE_WEIGHT = 2d;
	
	/**
	 * Weigh the ResolvedCoordinate, using distance as weight.
	 * @param item ResolvedCoordinate to weigh
	 * @param context Set of plain-named locations found in the document.
	 * @param options Options for configuring the weigher
	 * @return A weight for scoring this result against others.
	 */
	@Override
	public double weigh(
			ResolvedCoordinate item,
			Collection<LocationOccurrence> context,
			Options options) {
		
		return DISTANCE_WEIGHT / item.getVectorFromKnownLocation().getMagnitude();
	}
}
