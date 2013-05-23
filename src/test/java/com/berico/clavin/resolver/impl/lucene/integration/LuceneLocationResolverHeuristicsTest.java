package com.berico.clavin.resolver.impl.lucene.integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.resolver.LocationResolver;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.integration.ResolverHeuristicsTest;
import com.berico.clavin.resolver.lucene.LuceneLocationResolver;

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
 * LuceneLocationResolverHeuristicsTest.java
 * 
 *###################################################################*/

/**
 * Tests the mapping of location names into
 * {@link ResolvedLocation} objects as performed by
 * {@link LocationResolver#resolveLocations(List<String>)}.
 */
public class LuceneLocationResolverHeuristicsTest extends ResolverHeuristicsTest {
	
	public final static Logger logger = LoggerFactory.getLogger(LuceneLocationResolverHeuristicsTest.class);
	
	// objects required for running tests
	File indexDirectory;
	LuceneLocationResolver resolverNoHeuristics;
	LuceneLocationResolver resolverWithHeuristics;
	
	/**
	 * Instantiate two {@link LuceneLocationResolver} objects, one without
	 * context-based heuristic matching and other with it turned on.
	 * 
	 * @throws IOException
	 * @throws ParseException 
	 */
	@Before
	public void setUp() throws IOException, ParseException {
		indexDirectory = new File("./IndexDirectory");
		resolverNoHeuristics = new LuceneLocationResolver(indexDirectory, 1, 1);
		resolverWithHeuristics = new LuceneLocationResolver(indexDirectory, 5, 5);
	}

	@Override
	protected LocationResolver getResolver() {
		
		return resolverWithHeuristics;
	}
	
	
	/**
	 * Ensure we select the correct {@link ResolvedLocation} objects
	 * without using context-based heuristic matching.
	 * 
	 * Without heuristics, {@link LuceneLocationResolver} will default to
	 * mapping location name Strings to the matching
	 * {@link ResolvedLocation} object with the greatest population.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNoHeuristics() throws Exception {
		String[] locations = {"Haverhill", "Worcester", "Springfield", "Kansas City"};
		
		List<ResolvedLocation> resolvedLocations = 
			resolverNoHeuristics.resolveLocations(LuceneLocationResolverTest.makeOccurrencesFromNames(locations), false);
		
		assertEquals("LocationResolver chose the wrong \"Haverhill\"", HAVERHILL_MA, resolvedLocations.get(0).getGeoname().geonameID);
		assertEquals("LocationResolver chose the wrong \"Worcester\"", WORCESTER_UK, resolvedLocations.get(1).getGeoname().geonameID);
		assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_MO, resolvedLocations.get(2).getGeoname().geonameID);
		assertEquals("LocationResolver chose the wrong \"Kansas City\"", KANSAS_CITY_MO, resolvedLocations.get(3).getGeoname().geonameID);
	}

}
