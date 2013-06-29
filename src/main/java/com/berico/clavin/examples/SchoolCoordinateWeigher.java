package com.berico.clavin.examples;

import java.util.Collection;

import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.Options;
import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.gazetteer.FeatureCode;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.strategies.coordinates.ResolvedCoordinateWeigher;

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
 * SpotCoordinateWeigher.java
 * 
 *###################################################################*/

/**
 * An example of how to influence the results of Coordinate Resolution.
 */
public class SchoolCoordinateWeigher implements ResolvedCoordinateWeigher {

	/**
	 * An example of the use of a custom Coordinate Weigher.
	 * @param args not used.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// Register the weigher.
		GeoParserFactory.DefaultCoordinateWeighers.add(new SchoolCoordinateWeigher());
		
		// Get an instance of the GeoParser.
		GeoParser parser = GeoParserFactory.getDefault("IndexDirectory/");
		
		// Parse the sentence.
		ResolutionContext context = 
				parser.parse(
					"I'm looking for a nice school around here: 38.9532, -77.3392.");
		
		// Show the coordinate results.
		System.out.println(context.getCoordinates().get(0));
	}

	/**
	 * Given a coordinate, reward entries that have the FeatureCode "SCH" (school), 
	 * and penalize others.
	 * @param item the result to evaluate.
	 * @param context All co-occurring locations, to help provide context when weighing
	 * the coordinate.
	 * @param options If your weigher needs configuration, it can pull it from this
	 * object.
	 * @return weight used to influence the select of the coordinate.
	 */
	@Override
	public double weigh(
			ResolvedCoordinate item,
			Collection<LocationOccurrence> context, 
			Options options) {
		
		// If this is a Spot feature.
		if (item.getKnownLocation()
				.getFeatureCode()
				.equals(FeatureCode.SCH)){
			
			// Give it an arbitrary bonus of "3".
			return 3;
		}
		
		// Otherwise, penalize the item.
		return -3;
	}

}
