package com.berico.clavin.resolver.impl.lucene.integration;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;

import com.berico.clavin.resolver.LocationResolver;
import com.berico.clavin.resolver.integration.ResolverBehaviorTest;
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
 * LuceneLocationResolverTest.java
 * 
 *###################################################################*/

/**
 * Ensures non-heuristic matching and fuzzy matching features are
 * working properly in {@link LuceneLocationResolver}.
 *
 */
public class LuceneLocationResolverTest extends ResolverBehaviorTest {
	
	// objects required for running tests
	File indexDirectory;
	LuceneLocationResolver resolverNoHeuristics;
	
	/**
	 * Instantiate a {@link LuceneLocationResolver} without context-based
	 * heuristic matching and with fuzzy matching turned on.
	 * 
	 * @throws IOException
	 * @throws ParseException 
	 */
	@Before
	public void setUp() throws IOException, ParseException {
		// indexDirectory = new File("./src/test/resources/indices/GeoNamesSampleIndex");
		indexDirectory = new File("./IndexDirectory");
		resolverNoHeuristics = new LuceneLocationResolver(indexDirectory, 1, 1);
	}
	
	@Override
	protected LocationResolver getResolver() {
		
		return resolverNoHeuristics;
	}

}
