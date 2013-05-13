package com.berico.clavin.extractor;

import java.util.List;

public interface CoordinateExtractor {
	
	List<CoordinateOccurrence<?>> extractCoordinates(String text);
	
}
