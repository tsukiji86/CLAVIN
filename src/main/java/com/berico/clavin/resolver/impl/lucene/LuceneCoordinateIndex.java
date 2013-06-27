package com.berico.clavin.resolver.impl.lucene;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;

import com.berico.clavin.Options;
import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.CoordinateIndex;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Circle;

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
 * LuceneCoordinateIndex.java
 * 
 *###################################################################*/

/**
 * Coordinate Index backed by a Lucene 4+ index with a spatial field.
 * 
 * This implementation searches for locations that fall within a
 * specified radius of the target coordinate.  These locations, 
 * termed simply as "nearby", are added to the {@link OldImplOfResolvedCoordinate}
 * class.  It is another service's responsibility to decide what the 
 * 'actual resolved location' should be for the coordinate.
 */
public class LuceneCoordinateIndex implements CoordinateIndex {

	public static int DEFAULT_DISTANCE_KM = 20;
	public static String KEY_DEFAULT_DISTANCE_KM = "coord.index.distance";
	
	public static int DEFAULT_LIMIT = 5000;
	public static String KEY_DEFAULT_LIMIT = "coord.index.limit";
	
	protected LuceneComponents lucene;
	
	/**
	 * Instantiate the index with it's Lucene dependencies.
	 * @param lucene Lucene Components
	 */
	public LuceneCoordinateIndex(LuceneComponents lucene) {
		
		this.lucene = lucene;
	}

	/**
	 * Set the default distance from a known point we should
	 * find associated locations.
	 * @param km Distance in Kilometers.
	 */
	public void setDefaultDistance(int km){
		
		DEFAULT_DISTANCE_KM = km;
	}
	
	/**
	 * Search for locations around the supplied coordinate.
	 * @param coordinate Coordinate to search for nearby locations.
	 * @return ResolvedCoordinate instance.
	 */
	@Override
	public List<ResolvedCoordinate> search(
			CoordinateOccurrence<?> coordinate, Options options) throws Exception {
		
		// Guard against null.
		options = (options == null)? new Options() : options;
		
		int distance = 
				options.getInt(KEY_DEFAULT_DISTANCE_KM, DEFAULT_DISTANCE_KM);

		int limit = 
				options.getInt(KEY_DEFAULT_LIMIT, DEFAULT_LIMIT);
		
		return performSearch(coordinate, distance, limit);
	}
	
	/**
	 * Search for locations around the supplied coordinate.
	 * @param coordinate Coordinate to search for nearby locations.
	 * @param distanceInKm Kilometer radius to search around the
	 * target coordinate for named locations.
	 * @param limit Max number of results to return from the index.
	 * @return ResolvedCoordinate instance.
	 */
	List<ResolvedCoordinate> performSearch(
			CoordinateOccurrence<?> coordinate, 
			int distanceInKm,
			int limit)
			throws Exception {
		
		// Acquire a searcher.
		IndexSearcher searcher = this.lucene.getSearcherManager().acquire();
		
		// Convert the KM distance to degrees.
		double distanceInDegrees = 
			DistanceUtils.dist2Degrees(
				distanceInKm, DistanceUtils.EARTH_MEAN_RADIUS_KM);
		
		// Convert the coordinate to it's lat/lon representation.
		LatLon center = coordinate.convertToLatLon();
		
		// Create a circular bounding box using the coordinate as the center
		// and the distance as the circle's radius.
		Circle queryBoundary = 
				this.lucene.getSpatialContext().makeCircle(
				center.getLongitude(), center.getLatitude(), distanceInDegrees);
		
		// Create a spatial search configuration
		SpatialArgs spatialArgs = new SpatialArgs(
			SpatialOperation.Intersects, queryBoundary);
		
		// Get a Lucene filter from the spatial config.
		Filter filter = this.lucene.getSpatialStrategy().makeFilter(spatialArgs);
		
		// Search the index using the circle as a bounding box (er...circle).
		TopDocs results = searcher.search(
			new MatchAllDocsQuery(), filter, DEFAULT_LIMIT);
		
		// Convert the results to a ResolvedCoordinate
		return LuceneUtils.convertToCoordinate(
				coordinate, searcher, results, lucene);
	}

	/**
	 * Set the max radius in which to look for matches in the index.
	 * @param options Options to set on
	 * @param km Max distance in Kilometers
	 */
	public static void configureLookupDistance(Options options, int km){
		
		options.put(KEY_DEFAULT_DISTANCE_KM, Integer.toString(km));
	}
	
	/**
	 * Set the max number of results to return from the index.
	 * @param options Options to set on
	 * @param limit Max number of results to return.
	 */
	public static void configureLimit(Options options, int limit){
		
		options.put(KEY_DEFAULT_LIMIT, Integer.toString(limit));
	}
}
