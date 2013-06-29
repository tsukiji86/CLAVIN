package com.berico.clavin.resolver.impl;

import java.util.List;

import com.berico.clavin.Options;
import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.ResolvedLocation;

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
 * LocationNameIndex.java
 * 
 *###################################################################*/

/**
 * Defines the interface for performing searches for ResolvedLocation candidates 
 * based on the provided LocationOccurrence.
 */
public interface LocationNameIndex {
	
	List<ResolvedLocation> search(
			LocationOccurrence occurrence, Options options) throws Exception;
	
}
