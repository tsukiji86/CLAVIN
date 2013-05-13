package com.berico.clavin.extractor.coords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.code.regexp.Pattern;
import com.google.code.regexp.Matcher;

import com.berico.clavin.extractor.CoordinateExtractor;
import com.berico.clavin.extractor.CoordinateOccurrence;

public class RegexCoordinateExtractor implements CoordinateExtractor {
	
	ArrayList<RegexCoordinateParsingStrategy<?>> strategies 
		= new ArrayList<RegexCoordinateParsingStrategy<?>>();

	public RegexCoordinateExtractor(
			Collection<RegexCoordinateParsingStrategy<?>> strategies) {
		
		assert strategies != null;
		
		this.strategies.addAll(strategies);
	}

	@Override
	public List<CoordinateOccurrence<?>> extractCoordinates(String text) {
		
		ArrayList<CoordinateOccurrence<?>> occurrences = 
			new ArrayList<CoordinateOccurrence<?>>();
		
		for (RegexCoordinateParsingStrategy<?> strategy : this.strategies){
			
			Pattern pattern = strategy.getPattern();
			
			Matcher matcher = pattern.matcher(text);
			
			while (matcher.find()){
				
				CoordinateOccurrence<?> occurrence = 
					strategy.parse(matcher.group(), matcher.namedGroups(), matcher.start());
				
				occurrences.add(occurrence);
			}
		}
		
		return occurrences;
	}
}
