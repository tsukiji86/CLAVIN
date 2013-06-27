package com.berico.clavin.resolver.impl.lucene;

import com.berico.clavin.gazetteer.Place;

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
 * BuilderContext.java
 * 
 *###################################################################*/

/**
 * This is injected into the begin() method to make the use of the IndexBuilder
 * more intuitive.
 */
public interface BuilderContext {
	
	/**
	 * Add a Place to the index.
	 * @param place
	 */
	void add(Place place);
	
	/**
	 * Get the total number of records processed.
	 * @return Total records processed.
	 */
	long getTotalProcessed();
}