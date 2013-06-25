package com.berico.clavin.resolver.impl.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.store.FSDirectory;

import com.spatial4j.core.context.SpatialContext;

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
 * LuceneComponentsFactory.java
 * 
 *###################################################################*/

/**
 * Factory for instantiating all of the Lucene components necessary to perform
 * text-based and geospatial searches in CLAVIN.
 */
public class LuceneComponentsFactory {

	SearcherManager searcherManager;
	SpatialContext spatialContext;
	SpatialStrategy spatialStrategy;
	FSDirectory index;
	IndexSearcher indexSearcher;
	Analyzer indexAnalyzer;
	File indexDir;
	
	/**
	 * Instantiate with a handle to the local Lucene index.
	 * YOU MUST CALL INITIALIZE WHEN YOU ARE DONE!!!!
	 * 
	 * @param indexDirectory Directory of the Lucene index.
	 */
	public LuceneComponentsFactory(String indexDirectory){
		
		this.indexDir = new File(indexDirectory);
	}
	
	/**
	 * Initialize the SearcherManager (and other components).
	 * @return This object (it's fluent!).
	 * @throws IOException 
	 */
	public LuceneComponentsFactory initialize() throws IOException{
		
		// load the Lucene index directory from disk
		index = FSDirectory.open(indexDir);
		
		// index employs simple lower-casing & tokenizing on whitespace
		indexAnalyzer = new WhitespaceLowerCaseAnalyzer();
		indexSearcher = new IndexSearcher(DirectoryReader.open(index));
		
		// override default TF/IDF score to ignore multiple appearances
		indexSearcher.setSimilarity(new BinarySimilarity());
		
		// Retrieve the spatial context implementation from Spatial4j,
		// which will be used by Lucene to do it's Geo thing.
		spatialContext = SpatialContext.GEO;
		
		// Instantiate the spatial search strategy.
		spatialStrategy = 
			new RecursivePrefixTreeStrategy(
					new GeohashPrefixTree(spatialContext, 11), FieldConstants.GEOMETRY);
		
		// Instantiate the searcher manager.
		searcherManager = new SearcherManager(index, null);
		
		// Do it.
		return this;
	}
	
	/**
	 * Retrieve the essential Lucene components needed to perform a faceted search.
	 * @return  A wrapper class with all the necessary Lucene constructs.
	 */
	public  LuceneComponents getComponents(){
		
		return new LuceneComponents(
			searcherManager, spatialStrategy, spatialContext, indexAnalyzer);
	}
	
}
