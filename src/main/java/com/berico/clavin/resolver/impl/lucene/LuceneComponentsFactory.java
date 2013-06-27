package com.berico.clavin.resolver.impl.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

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
	IndexWriter indexWriter;
	
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
	 * Initialize the common stuff used by both the IndexWriter and 
	 * Searcher.
	 * @throws IOException
	 */
	protected void initializeCommon() throws IOException {
	
		// load the Lucene index directory from disk
		index = FSDirectory.open(indexDir);
		
		// index employs simple lower-casing & tokenizing on whitespace
		indexAnalyzer = new WhitespaceLowerCaseAnalyzer();
		
		// Retrieve the spatial context implementation from Spatial4j,
		// which will be used by Lucene to do it's Geo thing.
		spatialContext = SpatialContext.GEO;
		
		// Instantiate the spatial search strategy.
		spatialStrategy = 
			new RecursivePrefixTreeStrategy(
					new GeohashPrefixTree(spatialContext, 11), FieldConstants.GEOMETRY);
	}
	
	/**
	 * Initialize the IndexWriter (and other components).
	 * @return This object (it's needlessly fluent!).
	 * @throws IOException
	 */
	public LuceneComponentsFactory initializeWriter() throws IOException {
		
		initializeCommon();
		
		// instantiate the index writer
		indexWriter = new IndexWriter(index, 
				new IndexWriterConfig(Version.LUCENE_43, indexAnalyzer));
		
		return this;
	}
	
	
	/**
	 * Initialize the SearcherManager (and other components).
	 * @return This object (it's needlessly fluent!).
	 * @throws IOException 
	 */
	public LuceneComponentsFactory initializeSearcher() throws IOException{
		
		initializeCommon();
		
		// instantiate an index searcher
		indexSearcher = new IndexSearcher(DirectoryReader.open(index));
		
		// override default TF/IDF score to ignore multiple appearances
		indexSearcher.setSimilarity(new BinarySimilarity());
		
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

	/**
	 * Get the SearcherManager.
	 * @return SearcherManager.
	 */
	public SearcherManager getSearcherManager() {
		return searcherManager;
	}

	/**
	 * Get the Spatial4j Context.
	 * @return Spatial4j Context.
	 */
	public SpatialContext getSpatialContext() {
		return spatialContext;
	}

	/**
	 * Get the Spatial4j SpatialStrategy.
	 * @return SpatialStrategy.
	 */
	public SpatialStrategy getSpatialStrategy() {
		return spatialStrategy;
	}

	/**
	 * Get the Lucene File System directory.
	 * @return FS Directory.
	 */
	public FSDirectory getIndex() {
		return index;
	}

	/**
	 * Get the IndexSearcher.
	 * @return IndexSearcher.
	 */
	public IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	/**
	 * Get the Lucene Index Analyzer.
	 * @return Index Analyzer.
	 */
	public Analyzer getIndexAnalyzer() {
		return indexAnalyzer;
	}

	/**
	 * Get the Index Directory.
	 * @return File representing the index directory.
	 */
	public File getIndexDir() {
		return indexDir;
	}

	/**
	 * Get the Index Writer.
	 * @return Index Writer.
	 */
	public IndexWriter getIndexWriter() {
		return indexWriter;
	}
}
