package com.berico.clavin;

import java.util.ArrayList;
import java.util.Arrays;

import com.berico.clavin.extractor.LocationExtractor;
import com.berico.clavin.extractor.coords.DdPatternParsingStrategy;
import com.berico.clavin.extractor.coords.DmsPatternParsingStrategy;
import com.berico.clavin.extractor.coords.RegexCoordinateExtractor;
import com.berico.clavin.extractor.coords.RegexCoordinateParsingStrategy;
import com.berico.clavin.extractor.opennlp.ApacheExtractor;
import com.berico.clavin.resolver.LocationResolver;
import com.berico.clavin.resolver.impl.CoordinateCandidateSelectionStrategy;
import com.berico.clavin.resolver.impl.CoordinateIndex;
import com.berico.clavin.resolver.impl.DefaultLocationResolver;
import com.berico.clavin.resolver.impl.LocationCandidateSelectionStrategy;
import com.berico.clavin.resolver.impl.LocationNameIndex;
import com.berico.clavin.resolver.impl.ResolutionResultsReductionStrategy;
import com.berico.clavin.resolver.impl.lucene.LuceneComponents;
import com.berico.clavin.resolver.impl.lucene.LuceneComponentsFactory;
import com.berico.clavin.resolver.impl.lucene.LuceneCoordinateIndex;
import com.berico.clavin.resolver.impl.lucene.LuceneLocationNameIndex;
import com.berico.clavin.resolver.impl.strategies.IdentityReductionStrategy;
import com.berico.clavin.resolver.impl.strategies.WeightedCoordinateScoringStrategy;
import com.berico.clavin.resolver.impl.strategies.coordinates.SharedLocationNameWeigher;
import com.berico.clavin.resolver.impl.strategies.coordinates.VectorDistanceWeigher;
import com.berico.clavin.resolver.impl.strategies.locations.ContextualOptimizationStrategy;

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
 * GeoParserFactory.java
 * 
 *###################################################################*/

/**
 * Simple Factory for the creation of GeoParser instances.  The 'default'
 * instance being Apache OpenNLP as the Extractor and a local Lucene index
 * as the LocationResolver.
 */
public class GeoParserFactory {
	
	public static int MAX_HIT_DEPTH = 1;
	public static int MAX_CONTENT_WINDOW = 1;
	
	/**
	 * Get the default GeoParser.
	 * @param pathToLuceneIndex Path to the local Lucene index.
	 * @return GeoParser
	 * @throws Exception Most likely a IOException due to inaccessible Lucene index.
	 */
	public static GeoParser getDefault(String pathToLuceneIndex) throws Exception {
		
		return getDefault(pathToLuceneIndex, MAX_HIT_DEPTH, MAX_CONTENT_WINDOW, true);
	}
	
	/**
	 * Get the default GeoParser.
	 * @param pathToLuceneIndex Path to the local Lucene index.
	 * @param fuzzy Should fuzzy matching be used?
	 * @return GeoParser
	 * @throws Exception Most likely a IOException due to inaccessible Lucene index.
	 */
	public static GeoParser getDefault(
			String pathToLuceneIndex, boolean fuzzy) throws Exception {
		
		return getDefault(pathToLuceneIndex, MAX_HIT_DEPTH, MAX_CONTENT_WINDOW, fuzzy);
	}
	
	/**
	 * Get the default GeoParser.
	 * @param pathToLuceneIndex Path to the local Lucene index.
	 * @param maxHitDepth Number of candidate matches to consider
	 * @param maxContentWindow How much context to consider when resolving
	 * @return GeoParser
	 * @throws Exception Most likely a IOException due to inaccessible Lucene index.
	 */
	public static GeoParser getDefault(
			String pathToLuceneIndex, int maxHitDepth, int maxContentWindow)
					throws Exception {
		
		return getDefault(pathToLuceneIndex, maxHitDepth, maxContentWindow, false);
	}
	
	/**
	 * Get the default GeoParser.
	 * @param pathToLuceneIndex Path to the local Lucene index.
	 * @param maxHitDepth Number of candidate matches to consider
	 * @param maxContentWindow How much context to consider when resolving
	 * @param fuzzy Should fuzzy matching be used?
	 * @return GeoParser
	 * @throws Exception Most likely a IOException due to inaccessible Lucene index.
	 */
	public static GeoParser getDefault(
			String pathToLuceneIndex, int maxHitDepth, int maxContentWindow, boolean fuzzy) 
					 throws Exception {
		
		Options options = new Options();
		
		LuceneLocationNameIndex.configureLimit(options, maxHitDepth);
		LuceneLocationNameIndex.configureUseFuzzy(options, fuzzy);
		
		return getDefault(pathToLuceneIndex, options);
	}
	
	/**
	 * Get the default GeoParser
	 * @param pathToLuceneIndex Path to the local Lucene index.
	 * @param options Configuration for dependent services.
	 * @return GeoParser
	 * @throws Exception Most likely a IOException due to inaccessible Lucene index.
	 */
	public static GeoParser getDefault(
			String pathToLuceneIndex, Options options) throws Exception {
		
		LocationExtractor locationExtractor = new ApacheExtractor();
		
		ArrayList<RegexCoordinateParsingStrategy<?>> coordinateParsingStrategies = 
				new ArrayList<RegexCoordinateParsingStrategy<?>>();
		
		coordinateParsingStrategies.add(new DmsPatternParsingStrategy());
		coordinateParsingStrategies.add(new DdPatternParsingStrategy());
		
		RegexCoordinateExtractor coordinateExtractor = 
				new RegexCoordinateExtractor(coordinateParsingStrategies);
		
		LuceneComponentsFactory factory = new LuceneComponentsFactory(pathToLuceneIndex);
		
		factory.initializeSearcher();
		
		LuceneComponents lucene = factory.getComponents();
		
		LocationNameIndex locationNameIndex = new LuceneLocationNameIndex(lucene);
		
		CoordinateIndex coordinateIndex = new LuceneCoordinateIndex(lucene);
		
		@SuppressWarnings("unchecked")
		CoordinateCandidateSelectionStrategy coordinateSelectionStrategy = 
			new WeightedCoordinateScoringStrategy(
				Arrays.asList(
					new SharedLocationNameWeigher(), 
					new VectorDistanceWeigher()));
		
		LocationCandidateSelectionStrategy locationSelectionStrategy = 
				new ContextualOptimizationStrategy();
		
		ResolutionResultsReductionStrategy reductionStrategy = 
				new IdentityReductionStrategy();
		
		LocationResolver resolver = new DefaultLocationResolver(
				locationNameIndex, 
				coordinateIndex, 
				locationSelectionStrategy, 
				coordinateSelectionStrategy, 
				reductionStrategy);
		
		return new GeoParser(locationExtractor, coordinateExtractor, resolver);
	}
}
