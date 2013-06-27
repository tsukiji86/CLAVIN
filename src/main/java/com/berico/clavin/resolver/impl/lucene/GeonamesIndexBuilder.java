package com.berico.clavin.resolver.impl.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;

import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.util.GeonamesUtils;

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
 * GeonamesIndexBuilder.java
 * 
 *###################################################################*/

/**
 * Build a CLAVIN index from the Geonames gazetteer.  This process
 * assumes you have downloaded the Geonames gazetteer from this
 * url:  http://download.geonames.org/export/dump/allCountries.zip
 * and have extracted the text file.  We will convert each line of the
 * file into a Place entry, providing the record to the indexer for
 * processing.
 */
public class GeonamesIndexBuilder extends IndexBuilder {

	/**
	 * You have to have an entry point, and it can't be inherited
	 * from a super class.
	 * @param args Command line arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		
		// New up our class passing the arguments in.
		new GeonamesIndexBuilder(args);
	}
	
	private String geonamesSourceFile;
	private BufferedReader reader;
	
	/**
	 * Instantiate the IndexBuilder with the command line arguments.
	 * @param args Command line arguments.
	 * @throws Exception
	 */
	public GeonamesIndexBuilder(String[] args) throws Exception { super(args); }

	/**
	 * The description of what this indexer does.
	 * @return Description
	 */
	@Override
	String getDescription() {
		
		return "Converts the Geonames gazetteer into a CLAVIN index.";
	}

	/**
	 * The super class provides the argument parser before parsing the input.
	 * This allows us to register our own parameters prior to the input being
	 * parsed.
	 * @param parser Argument parser to register new arguments with.
	 */
	@Override
	void extend(ArgumentParser parser) {
		
		 // Register the Geonames source file. 
		 parser.addArgument("source")
		 	.metavar("geonames-file")
		 	.type(String.class)
		 	.required(true)
		 	.help("Geonames gazetteer source location.");
	}
	
	/**
	 * Initialize the index builder with the provided command line input
	 * preparsed for us.  In our case, we only want to collect the location of
	 * the source file.
	 * @param namespace The object containing the parsed input.
	 */
	@Override
	void initialize(Namespace namespace) {
		
		geonamesSourceFile = namespace.getString("source");
	}

	/**
	 * Being the process of converting the Geonames source file into Place
	 * objects that can be added to the CLAVIN index.
	 * @param context This is a helper we can use to emit places and find
	 * out how many records we've already processed.
	 */
	@Override
	void begin(BuilderContext context) throws Exception {
		
		// Initialize the reader.
		reader = 
			new BufferedReader(
				new InputStreamReader(
					new FileInputStream( 
						new File(geonamesSourceFile)), "UTF-8" ));
		
		String line;
		
		// Iterate over each line of the file.
		while ((line = reader.readLine()) != null){
			
			// Parse the line into a Place object
			Place place = GeonamesUtils.parseFromGeoNamesRecord(line);
			
			// Add the place object to the BuilderContext
			context.add(place);
		}
	}

	/**
	 * When the index creation process is done, the super class gives us
	 * an opportunity to clean up resources.  Here we'll close the reader.
	 */
	@Override
	void cleanup() throws Exception {
		
		reader.close();
	}
	
}
