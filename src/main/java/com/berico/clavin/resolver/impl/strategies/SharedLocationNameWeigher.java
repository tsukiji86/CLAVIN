package com.berico.clavin.resolver.impl.strategies;

import java.util.Collection;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedCoordinate;


public class SharedLocationNameWeigher 
	implements Weigher<ResolvedCoordinate, Collection<LocationOccurrence>> {

	public static double SHARED_LOCATION_FACTOR = 3;
	
	@Override
	public double weigh(ResolvedCoordinate item,
			Collection<LocationOccurrence> context) {
		
		double bestMatch = -1;
		
		for (LocationOccurrence occurrence : context){
			
			double matchScore = getMatchScore(
				item.getKnownLocation().alternateNames, occurrence.getText());
			
			if (matchScore > bestMatch) 
				bestMatch = matchScore;
			
		}

		return SHARED_LOCATION_FACTOR * bestMatch;
	}
	
	protected double getMatchScore(Collection<String> collection, String item){
		
		for (String collectionItem : collection){
			
			if(collectionItem.equalsIgnoreCase(item)){
				
				return 1d;
			}
			
			String shorter = item;
			String longer = collectionItem;
			
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
