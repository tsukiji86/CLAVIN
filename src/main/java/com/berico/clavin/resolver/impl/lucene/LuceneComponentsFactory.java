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

import com.berico.clavin.index.BinarySimilarity;
import com.berico.clavin.index.WhitespaceLowerCaseAnalyzer;
import com.spatial4j.core.context.SpatialContext;

public class LuceneComponentsFactory {

	SearcherManager searcherManager;
	SpatialContext spatialContext;
	SpatialStrategy spatialStrategy;
	FSDirectory index;
	IndexSearcher indexSearcher;
	Analyzer indexAnalyzer;
	File indexDir;
	
	public LuceneComponentsFactory(String indexDirectory){
		
		this.indexDir = new File(indexDirectory);
	}
	
	public LuceneComponentsFactory initialize() throws IOException{
		
		// load the Lucene index directory from disk
		index = FSDirectory.open(indexDir);
		
		// index employs simple lower-casing & tokenizing on whitespace
		indexAnalyzer = new WhitespaceLowerCaseAnalyzer();
		indexSearcher = new IndexSearcher(DirectoryReader.open(index));
		
		// override default TF/IDF score to ignore multiple appearances
		indexSearcher.setSimilarity(new BinarySimilarity());
		
		spatialContext = SpatialContext.GEO;
		
		spatialStrategy = 
			new RecursivePrefixTreeStrategy(
					new GeohashPrefixTree(spatialContext, 11), "geometry");
		
		searcherManager = new SearcherManager(index, null);
		
		return this;
	}
	
	
	public  LuceneComponents getComponents(){
		
		return new LuceneComponents(searcherManager, spatialStrategy, spatialContext);
	}
	
}
