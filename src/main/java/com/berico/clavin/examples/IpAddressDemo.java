package com.berico.clavin.examples;

import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;

public class IpAddressDemo {

	
	public static void main(String[] args) throws Exception {
		
		// Register the new Parsing Strategy.
		GeoParserFactory
			.DefaultCoordinateParsingStrategies
				.add(new IpAddressParsingStrategy());
		
		// Get a parser instance.
		GeoParser parser = GeoParserFactory.getDefault("IndexDirectory/");
		
		
		ResolutionContext results = 
			parser.parse("21.1.1.1   Foo 34.55.44.22   asfsadfadfadfas");
		
		
		// Get the CoordinateOccurrence from the Extraction Context
		CoordinateOccurrence<?> ecoord = 
				results.getExtractionContext().getCoordinates().get(0);
		
		System.out.println(ecoord);
		
		// Get the ResolvedCoordinate from the results
		ResolvedCoordinate rcoord = results.getCoordinates().get(0);
		
		System.out.println(rcoord);
	}
}
