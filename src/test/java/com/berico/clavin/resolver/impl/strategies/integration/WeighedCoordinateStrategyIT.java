package com.berico.clavin.resolver.impl.strategies.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.extractor.coords.LatLonOccurrence;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.CoordinateCandidateSelectionStrategy;
import com.berico.clavin.resolver.impl.lucene.LuceneComponents;
import com.berico.clavin.resolver.impl.lucene.LuceneComponentsFactory;
import com.berico.clavin.resolver.impl.lucene.LuceneCoordinateIndex;
import com.berico.clavin.resolver.impl.strategies.WeightedCoordinateScoringStrategy;
import com.berico.clavin.resolver.impl.strategies.coordinates.SharedLocationNameWeigher;
import com.berico.clavin.resolver.impl.strategies.coordinates.VectorDistanceWeigher;

public class WeighedCoordinateStrategyIT {

	LuceneCoordinateIndex coordinateIndex;
	CoordinateCandidateSelectionStrategy strategy;
	
	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		
		LuceneComponents lucene = 
				new LuceneComponentsFactory("./IndexDirectory/")
					.initializeSearcher()
					.getComponents();
			
		coordinateIndex = new LuceneCoordinateIndex(lucene);
		
		strategy = new WeightedCoordinateScoringStrategy(
				Arrays.asList(
					new SharedLocationNameWeigher(), 
					new VectorDistanceWeigher()));
	}

	@Test
	public void test() throws Exception {
		
		List<LocationOccurrence> occurrences = 
				buildOccurrences("Reston", "Fairfax", "Manassas");
		
		LatLonOccurrence reston = 
				new LatLonOccurrence("38.9686° N, 77.3414° W", -1, 
					new LatLon(38.9686, -77.3414));
			
		List<ResolvedCoordinate> coordinates = coordinateIndex.search(reston, null);
		
		System.out.println(coordinates.size());
		
		@SuppressWarnings("unchecked")
		List<List<ResolvedCoordinate>> candidateLists = Arrays.asList(coordinates);
		
		List<ResolvedCoordinate> coords = strategy.select(candidateLists, occurrences, null);
		
		System.out.println(coords.get(0).getKnownLocation());
	}

	
	private static List<LocationOccurrence> 
		buildOccurrences(String... foundLocations){
		
		ArrayList<LocationOccurrence> occurrences = 
				new ArrayList<LocationOccurrence>();
	
		int count = 0;
		
		for(String location : foundLocations){
			
			count++;
			
			occurrences.add(new LocationOccurrence(location, count));
		}
		
		return occurrences;
	}
	
}
