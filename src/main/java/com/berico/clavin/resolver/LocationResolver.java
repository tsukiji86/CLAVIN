package com.berico.clavin.resolver;

import com.berico.clavin.Options;
import com.berico.clavin.extractor.ExtractionContext;
import com.berico.clavin.extractor.LocationExtractor;

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
 * LocationResolver.java
 * 
 *###################################################################*/

/**
 * Resolves location names into GeoName objects.
 * 
 * Takes location names extracted from unstructured text documents by
 * {@link LocationExtractor} and resolves them into the appropriate
 * geographic entities (as intended by the document's author based on
 * context) by finding the best match in a gazetteer.
 * 
 */
public interface LocationResolver {
	
	/**
	 * Resolves the locations and coordinates specified in the ExtractionContext
	 * into a {@link ResolutionContext} which contains a list of {@link ResolvedLocation}s
	 * and {@link ResolvedCoordinate}s.
	 * 
	 * @param extractionContext Information related to the extraction of 
	 * locations and coordinates.
	 * @return The results of resolving locations and coordinates.
	 * @throws Exception
	 */
	 ResolutionContext resolveLocations(
			ExtractionContext extractionContext) throws Exception;
	
	/**
	 * Resolves the locations and coordinates specified in the ExtractionContext
	 * into a {@link ResolutionContext} which contains a list of {@link ResolvedLocation}s
	 * and {@link ResolvedCoordinate}s.
	 * 
	 * @param extractionContext Information related to the extraction of 
	 * locations and coordinates.
	 * @param options Options for configuring the underlying resolver.
	 * @return The results of resolving locations and coordinates.
	 * @throws Exception
	 */
	 ResolutionContext resolveLocations(
			ExtractionContext extractionContext, Options options) throws Exception;
	
}
