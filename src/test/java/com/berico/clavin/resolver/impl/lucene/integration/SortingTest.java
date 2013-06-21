package com.berico.clavin.resolver.impl.lucene.integration;

import java.io.IOException;

import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.index.WhitespaceLowerCaseAnalyzer;
import com.berico.clavin.resolver.impl.lucene.FieldConstants;
import com.berico.clavin.resolver.impl.lucene.LuceneComponents;
import com.berico.clavin.resolver.impl.lucene.LuceneComponentsFactory;
import com.berico.clavin.util.GeonamesUtils;

public class SortingTest {

	@Test
	public void test() throws IOException, ParseException {
		
		LuceneComponents lc = 
			new LuceneComponentsFactory("IndexDirectory/")
				.initialize()
				.getComponents();
		
		IndexSearcher searcher = lc.getSearcherManager().acquire();
		
		Query q = new AnalyzingQueryParser(
				Version.LUCENE_43, 
				FieldConstants.NAME, 
				new WhitespaceLowerCaseAnalyzer()).parse("\"new york\"");
		
		TopDocs td = searcher.search(q, null, 100, 
			new Sort(
				new SortField(FieldConstants.POPULATION, SortField.Type.LONG, true),
				SortField.FIELD_SCORE));
		
		System.out.println(td.totalHits);
		
		for (ScoreDoc doc : td.scoreDocs){
			
			String geoname = searcher.doc(doc.doc).get(FieldConstants.GEONAME);
			
			Place g = GeonamesUtils.parseFromGeoNamesRecord(geoname);
			
			System.out.println(g);
		}
	}

}
