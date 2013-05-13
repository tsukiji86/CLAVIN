package com.berico.clavin.extractor.coords;

import java.util.Map;

import com.google.code.regexp.Pattern;

import com.berico.clavin.extractor.CoordinateOccurrence;

public interface RegexCoordinateParsingStrategy<T> {

	Pattern getPattern();
	
	CoordinateOccurrence<T> parse(
		String matchedString, Map<String, String> namedGroups, int startPosition);
	
}
