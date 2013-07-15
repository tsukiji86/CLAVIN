package com.berico.clavin.resolver.impl.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.Vector;
import com.berico.clavin.util.DamerauLevenshtein;
import com.berico.clavin.util.Serializer;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;

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
 * LuceneUtils.java
 * 
 *###################################################################*/

/**
 * A set of utilities for working with the Lucene index.
 */
public class LuceneUtils {

	/**
	 * Convert a set of Lucene Document Results into a list of
	 * ResolvedCoordinates.
	 * @param occurrence the CoordinateOccurrence in the document.
	 * @param searcher the Lucene Searcher that retrieved the results.
	 * @param results the Search results context
	 * @param components LuceneComponents (specifically, the spatial components
	 * needed to calculate vectors).
	 * @return a List of ResolvedCoordinates.
	 * @throws Exception 
	 */
	public static List<ResolvedCoordinate> convertToCoordinate(
			CoordinateOccurrence<?> occurrence,
			IndexSearcher searcher,
			TopDocs results,
			LuceneComponents components) throws Exception {
		
		// Results
		ArrayList<ResolvedCoordinate> resolvedCoordinates = 
				new ArrayList<ResolvedCoordinate>();
		
		// Grab the Lucene spatial context
		SpatialContext spatialContext = components.getSpatialContext();
		
		// If we have results
		if (results.scoreDocs.length > 0){
			
			// Get the center coordinate of the location occurrence
			LatLon center = occurrence.convertToLatLon();
			
			// Convert to a Spatial4j point
			Point occurrencePoint = 
				spatialContext.makePoint(
					center.getLongitude(), center.getLatitude());
			
			// Iterate over the results
			for (int i = 0; i < results.scoreDocs.length; i++){
			
				// Grab the document from Lucene
				Document doc = searcher.doc(results.scoreDocs[i].doc);
				
				// Get the Place record
				Place record = dehydrate(doc);
				
				// Get the centroid of the Place
				String positionOfLocation = doc.get(FieldConstants.GEOMETRY);
				
				// TODO: Spatial4J supposedly has a ShapeReaderWriter implementation
				// that we should be using instead.
				@SuppressWarnings("deprecation")
				Point point = (Point) spatialContext.readShape(positionOfLocation);
				
				// Calculate the distance
				double distanceInDegrees = 
						spatialContext.getDistCalc().distance(point, occurrencePoint);
				
				double distanceInKm = DistanceUtils.degrees2Dist(
						distanceInDegrees, DistanceUtils.EARTH_MEAN_RADIUS_KM);
				
				// Calculate the direction
				double direction = calculateDirection(
					point.getX(), point.getY(), 
					occurrencePoint.getX(), occurrencePoint.getY());
				
				// Add the ResolvedCoordinate to the list.
				resolvedCoordinates.add(
					new ResolvedCoordinate(occurrence, record, 
						new Vector(distanceInKm, direction)));
			}
		}
		
		return resolvedCoordinates;
	}
	
	/**
	 * Calculate the direction from a center point "c" to an offset point "o".
	 * @param cx Center X
	 * @param cy Center Y
	 * @param ox Offset X
	 * @param oy Offset Y
	 * @return Direction in degrees.
	 */
	private static double calculateDirection(double cx, double cy, double ox, double oy){
		
		// Direction of a Vector: tanθ = (y2 - y1) / (x2 - x1)
		// direction (i.e. θ) = tan^-1 * ((y2 - y1) / (x2 - x1))
		
		double dy = oy - cy;
		double dx = ox - cx;
		
		double theta = Math.atan2(dy, dx);
		
		// Convert to degrees
		double angle = Math.toDegrees( theta );
		
		// atan2 will produce negative degrees for coordinates west (left of or in quadrant
		// III or IV) of center.  We need to normalize the values by adding 2 * PI,
		// which in degrees happens to be 360.
		// Note:  we'll let 0 degrees be represented as 360.
		if (angle <= 0){
			
			angle = 360 + angle;
		}
		
		return angle;
	}
	
	/**
	 * Convert a set of Lucene Document Results into a list of ResolvedLocations.
	 * @param occurrence LocationOccurrence in the document.
	 * @param searcher the Lucene Searcher used to find the locations.
	 * @param results the results of the Lucene Search
	 * @param usingFuzzy whether fuzzy matching was used
	 * @return List of ResolvedLocations
	 * @throws IOException
	 */
	public static List<ResolvedLocation> convertToLocations(
			LocationOccurrence occurrence, 
			IndexSearcher searcher, 
			TopDocs results,
			boolean usingFuzzy) throws IOException {
		
		ArrayList<ResolvedLocation> locations = new ArrayList<ResolvedLocation>();
		
	 	if (results.scoreDocs.length > 0) {
	    		
	    		for (int i = 0; i < results.scoreDocs.length; i++) {
	    			
	    			Document doc = searcher.doc(results.scoreDocs[i].doc);
	    			
	    			ResolvedLocation location = convertToLocation(doc, occurrence, usingFuzzy);
	    		
	    			locations.add(location);
	    		}
	 	}
	 	
		return locations;
	}
	
	/**
	 * Convert a single index entry into a ResolvedLocation
	 * @param document the index entry
	 * @param location the LocationOccurrence within a document
	 * @param fuzzy whether fuzzy matching was used.
	 * @return ResolvedLocation
	 */
	public static ResolvedLocation convertToLocation(
			Document document, LocationOccurrence location, boolean fuzzy){
	
		Place place = dehydrate(document);
		
		String matchedName = document.get(FieldConstants.NAME);
		
		float confidence = 
			DamerauLevenshtein
				.damerauLevenshteinDistanceCaseInsensitive(
					location.getText(), matchedName);
		
		return new ResolvedLocation(matchedName, place, location, fuzzy, confidence);
	}
	
	/**
	 * Dehydrate a Place object from the Lucene index using the default
	 * serializer.
	 * @param document Document with the Place field to dehydrate.
	 * @return Place object.
	 */
	public static Place dehydrate(Document document){
		
		String serializedPlace = document.get(FieldConstants.PLACE);
		
		return Serializer.Default.deserialize(serializedPlace, Place.class);
	}
}
