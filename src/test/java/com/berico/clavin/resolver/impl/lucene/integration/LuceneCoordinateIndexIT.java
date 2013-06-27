package com.berico.clavin.resolver.impl.lucene.integration;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.berico.clavin.extractor.coords.LatLonOccurrence;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.lucene.LuceneComponents;
import com.berico.clavin.resolver.impl.lucene.LuceneComponentsFactory;
import com.berico.clavin.resolver.impl.lucene.LuceneCoordinateIndex;

public class LuceneCoordinateIndexIT {
	
	LuceneCoordinateIndex coordinateIndex;
	
	@Before
	public void setUp() throws Exception {
		
		LuceneComponents lucene = 
			new LuceneComponentsFactory("./IndexDirectory/")
				.initializeSearcher()
				.getComponents();
		
		coordinateIndex = new LuceneCoordinateIndex(lucene);
	}

	@Test
	public void test() throws Exception {
		
		LatLonOccurrence occurrence = 
			new LatLonOccurrence(-1, "23.211058, -109.653542",
				new LatLon(23.211058, -109.653542));
		
		List<ResolvedCoordinate> coordinate = coordinateIndex.search(occurrence, null);
		
		System.out.println(coordinate);
	}

}
