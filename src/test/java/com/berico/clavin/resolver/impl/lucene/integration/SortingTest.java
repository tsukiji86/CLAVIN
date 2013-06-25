package com.berico.clavin.resolver.impl.lucene.integration;

import java.util.List;

import org.junit.Test;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.impl.lucene.LuceneComponents;
import com.berico.clavin.resolver.impl.lucene.LuceneComponentsFactory;
import com.berico.clavin.resolver.impl.lucene.LuceneLocationNameIndex;

public class SortingTest {

	@Test
	public void test() throws Exception {
		
		LuceneComponents lc = 
			new LuceneComponentsFactory("IndexDirectory/")
				.initialize()
				.getComponents();
		
		LuceneLocationNameIndex index = new LuceneLocationNameIndex(lc);
		
		List<ResolvedLocation> results = 
				index.search(new LocationOccurrence("Boston", -1), 100, true);
		
		System.out.println("Return " + results.size() + " results.");
		
		for(ResolvedLocation loc : results){
			
			System.out.println(loc);
		}
	}

}
