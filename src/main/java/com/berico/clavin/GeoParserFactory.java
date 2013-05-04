package com.berico.clavin;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import com.berico.clavin.extractor.ApacheExtractor;
import com.berico.clavin.extractor.LocationExtractor;
import com.berico.clavin.resolver.LocationResolver;
import com.berico.clavin.resolver.lucene.LuceneLocationResolver;

public class GeoParserFactory {

	public static GeoParser getDefault(String pathToLuceneIndex) 
			throws IOException, ParseException{
		
		return getDefault(pathToLuceneIndex, 1, 1, false);
	}
	
	public static GeoParser getDefault(
			String pathToLuceneIndex, boolean fuzzy) 
					throws IOException, ParseException{
		
		return getDefault(pathToLuceneIndex, 1, 1, fuzzy);
	}
	
	public static GeoParser getDefault(
			String pathToLuceneIndex, int maxHitDepth, int maxContentWindow) 
					throws IOException, ParseException{
		
		return getDefault(pathToLuceneIndex, maxHitDepth, maxContentWindow, false);
	}
	
	public static GeoParser getDefault(
			String pathToLuceneIndex, int maxHitDepth, int maxContentWindow, boolean fuzzy) 
					throws IOException, ParseException{
		
		LocationExtractor extractor = new ApacheExtractor();
		
		LocationResolver resolver = new LuceneLocationResolver(
				new File(pathToLuceneIndex), maxHitDepth, maxContentWindow);
		
		return new GeoParser(extractor, resolver, fuzzy);
	}
}
