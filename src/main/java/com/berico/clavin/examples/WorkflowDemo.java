package com.berico.clavin.examples;

import java.io.File;

import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.util.TextUtils;

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
 * WorkflowDemo.java
 * 
 *###################################################################*/

/**
 * Quick example showing how to use CLAVIN's capabilities.
 * 
 */
public class WorkflowDemo {

	/**
	 * Run this after installing & configuring CLAVIN to get a sense of
	 * how to use it.
	 * 
	 * @param args				not used
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// Instantiate the CLAVIN GeoParser
		GeoParser parser = GeoParserFactory.getDefault("./IndexDirectory");
		
		// Read a file to string.
		String input = TextUtils.fileToString( 
				new File( "src/test/resources/sample-docs/Somalia-doc.txt" ) );
		
		// Perform the extraction and resolution workflow.
		ResolutionContext results = parser.parse(input);
		
		// Display the ResolvedLocations found for the location names
		for (ResolvedLocation location : results.getLocations()){
			
			String msg = String.format(
		      "%s [%s, %s] was mentioned at character position %s", 
		      location.getPlace().getName(), 
		      location.getPlace().getCenter().getLatitude(),
		      location.getPlace().getCenter().getLongitude(),
		      location.getLocation().getPosition());
				 
			System.out.println( msg );
		}
		
		// And we're done...
		System.out.println("\n\"That's all folks!\"");
	}
}
