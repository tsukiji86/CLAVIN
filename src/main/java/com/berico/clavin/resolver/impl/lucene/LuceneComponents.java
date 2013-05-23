package com.berico.clavin.resolver.impl.lucene;

import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.spatial.SpatialStrategy;

import com.spatial4j.core.context.SpatialContext;

public class LuceneComponents {

	protected SearcherManager searcherManager;
	protected SpatialStrategy spatialStrategy;
	protected SpatialContext spatialContext;
	
	public LuceneComponents(
			SearcherManager searcherManager,
			SpatialStrategy spatialStrategy, 
			SpatialContext spatialContext) {
		
		this.searcherManager = searcherManager;
		this.spatialStrategy = spatialStrategy;
		this.spatialContext = spatialContext;
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
}
