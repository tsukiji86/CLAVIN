package com.berico.clavin.resolver.impl.strategies;

import java.util.Collection;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;

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
* SharedLocationNameWeigher.java
*
*###################################################################*/

/**
 * Improve the resolution rank of a plain-named location match against
 * a Coordinate, if that plain-named location occurred in the document
 * as well.
 */
public class SharedLocationNameWeigher 
	implements Weigher<ResolvedCoordinate, Collection<LocationOccurrence>> {

	// Arbitrary multiplier applied to the best matching score.
	public static double SHARED_LOCATION_MULTIPLIER = 3;
	
	/**
	 * Weigh the ResolvedCoordinate, using the set of location occurrences
	 * as extra context.
	 * @param item ResolvedCoordinate to weigh
	 * @param context Set of plain-named locations found in the document.
	 * @return A weight for scoring this result against others.
	 */
	@Override
	public double weigh(
			ResolvedCoordinate item,
			Collection<LocationOccurrence> context) {
		
		double bestMatch = -1;
		
		// Find the best match.
		for (LocationOccurrence occurrence : context){
			
			double matchScore = getMatchScore(
				item.getKnownLocation().getAlternateNames(), occurrence.getText());
			
			if (matchScore > bestMatch) bestMatch = matchScore;
		}

		// Return the best match value with an added multiplier
		return SHARED_LOCATION_MULTIPLIER * bestMatch;
	}
	
	/**
	 * Determine how much of a match an alternate name is to a location
	 * occurrence (a value between 0 and 1).  This similarity is based on
	 * the occurrence of a substring and the number of characters different
	 * between the strings.  The smaller the differentiation, the better the
	 * score.
	 * @param collection Target collection to compare to the item.
	 * @param item Item to compare.
	 * @return Best comparison value.
	 */
	protected double getMatchScore(Collection<String> collection, String item){
		
		for (String collectionItem : collection){
			
			if(collectionItem.equalsIgnoreCase(item)){
				
				return 1d;
			}
			
			String shorter = item;
			String longer = collectionItem;
			
			// Swap strings to so we look for the smaller string
			// in the larger one.
			if (item.length() > collectionItem.length()){
				
				shorter = collectionItem;
				longer = item;
			}
			
			if(longer.toLowerCase().contains(
					shorter.toLowerCase())){
				
				int difference = longer.length() - shorter.length();
				
				// Return the % different based on string size
				return (longer.length() - difference) / longer.length();
			}
		}
		
		return 0d;
	}
}
