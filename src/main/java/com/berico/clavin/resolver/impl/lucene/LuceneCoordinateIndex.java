package com.berico.clavin.resolver.impl.lucene;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.CoordinateIndex;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Circle;

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
	public static int DEFAULT_NUMBER_OF_RESULTS = 5000;
	
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
			CoordinateOccurrence<?> coordinate) throws Exception {
		
		return this.search(coordinate, DEFAULT_DISTANCE_KM);
	}
	
	/**
	 * Search for locations around the supplied coordinate.
	 * @param coordinate Coordinate to search for nearby locations.
	 * @param distanceInKm Kilometer radius to search around the
	 * target coordinate for named locations.
	 * @return ResolvedCoordinate instance.
	 */
	@Override
	public List<ResolvedCoordinate> search(
			CoordinateOccurrence<?> coordinate, 
			int distanceInKm)
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
			new MatchAllDocsQuery(), filter, DEFAULT_NUMBER_OF_RESULTS);
		
		// Convert the results to a ResolvedCoordinate
		return LuceneUtils.convertToCoordinate(
				coordinate, searcher, results, lucene);
	}

}
