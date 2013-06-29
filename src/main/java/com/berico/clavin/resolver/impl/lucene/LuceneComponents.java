package com.berico.clavin.resolver.impl.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.spatial.SpatialStrategy;

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
 * LuceneComponents.java
 * 
 *###################################################################*/

public class LuceneComponents {

	protected SearcherManager searcherManager;
	protected SpatialStrategy spatialStrategy;
	protected SpatialContext spatialContext;
	protected Analyzer indexAnalyzer;
	
	public LuceneComponents(
			SearcherManager searcherManager,
			SpatialStrategy spatialStrategy, 
			SpatialContext spatialContext,
			Analyzer indexAnalyzer) {
		
		this.searcherManager = searcherManager;
		this.spatialStrategy = spatialStrategy;
		this.spatialContext = spatialContext;
		this.indexAnalyzer = indexAnalyzer;
	}

	public SearcherManager getSearcherManager() {
		return searcherManager;
	}

	public SpatialStrategy getSpatialStrategy() {
		return spatialStrategy;
	}

	public SpatialContext getSpatialContext() {
		return spatialContext;
	}
	
	public Analyzer getIndexAnalyzer(){
		return indexAnalyzer;
	}
}
