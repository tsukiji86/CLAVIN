package com.berico.clavin.examples;

import java.util.Arrays;

import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;

import com.berico.clavin.gazetteer.CountryCode;
import com.berico.clavin.gazetteer.FeatureClass;
import com.berico.clavin.gazetteer.FeatureCode;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.resolver.impl.lucene.BuilderContext;
import com.berico.clavin.resolver.impl.lucene.IndexBuilder;

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
 * CustomIndexBuilder.java
 * 
 *###################################################################*/

/**
 * Really simple example of how to create an index.
 */
public class CustomIndexBuilder extends IndexBuilder {

	/**
	 * You can't inherit a static method, so you will have to
	 * declare the main method in your class.
	 * @param args Command line arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// Instantiate the class passing in the arguments.
		new CustomIndexBuilder(args);
	}
	
	/**
	 * Something stupid to add to the index for example purposes.
	 */
	private String tagline;
	
	/**
	 * Pass the args to the super class.
	 * @param args Commmand line arguments.
	 * @throws Exception
	 */
	public CustomIndexBuilder(String[] args) throws Exception {
		super(args);
	}
	
	/**
	 * Provide a description of what your IndexBuilder will do.
	 * @return Description for the console.
	 */
	@Override
	protected String getDescription() {
		
		return "There's only one reliable place to find Cliff.";
	}

	/**
	 * You are given the ability to register for your own variables.  This is the
	 * preferred way for you to get configuration for initializing your IndexBuilder.
	 * @param parser ArgParse4j Parser.
	 */
	@Override
	protected void extend(ArgumentParser parser) {
		
		parser.addArgument("tagline")
			.metavar("Tagline")
			.type(String.class)
			.required(true)
			.help("Something you want to say about this place.");
		
		parser.addArgument("ssn")
			.metavar("SocialSecurityNumber")
			.type(String.class)
			.required(true)
			.help("SSN of user, you don't need to know why we collect this!");
	}

	/**
	 * Collect your configuration from the command line arguments.
	 * @param namespace A poorly named object which should be called 'ArgumentBucket'.
	 */
	@Override
	protected void initialize(Namespace namespace) {
		
		tagline = namespace.getString("tagline");
		
		String ssn = namespace.getString("ssn");
		
		pl("Can you believe it?  This guy just gave me his SSN: %s", ssn);
	}

	/**
	 * The super class has initialized, and is now passing control back to
	 * the CustomIndexBuilder.  All you need to do is use your configuration to
	 * call some datasource and convert that data into {@link Place} objects.
	 * 
	 * The context is a simple mechanism to allow you to add Places without needing
	 * to know the internals of how the IndexBuilder works.  It ever provides a
	 * counter, letting you know how many records have been processed.
	 * 
	 * In the future, if new functionality is exposed, it will be in the context,
	 * alleviating you from having to read and understand the workflow of IndexBuilder.
	 * 
	 * @param context Represents the actions and things you need to know while indexing.
	 */
	@Override
	protected void begin(BuilderContext context) throws Exception {
		
		Place cheersBoston = new Place();
		
		cheersBoston.setName("Cheers Bar and Grill, Boston");
		
		cheersBoston.setAsciiName("Cheers");
		
		cheersBoston.setAlternateNames(
			Arrays.asList(
				"A place where everbody knows you name",
				"A place where everbody's glad you came"));
		
		cheersBoston.setCenter(new LatLon(42.355927, -71.071137));
		
		cheersBoston.setPrimaryCountryCode(CountryCode.US);
		
		// You see, you can put whatever it is you want here.
		cheersBoston.setContext(tagline);
		
		// A Spot location.
		cheersBoston.setFeatureClass(FeatureClass.S);
		
		// A Restaurant.
		cheersBoston.setFeatureCode(FeatureCode.REST);
		
		// I doubt this place ever has 35 patrons.
		cheersBoston.setPopulation(35);
		
		// Add the place to the context to get indexed.
		context.add(cheersBoston);
		
		// You can get the record count at any time.
		pl("Wrote %s record(s).", context.getTotalProcessed());
	}

	/**
	 * This is your opportunity to clean up any resources.  It's guaranteed to be
	 * called even if an exception is thrown in begin();
	 */
	@Override
	protected void cleanup() throws Exception {
		
		pl("Cleaning up the joint, throwing out the drunks.");
	}

}
