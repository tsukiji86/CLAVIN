package com.berico.clavin.resolver.impl.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.extractor.coords.LatLonPair;
import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.Vector;
import com.berico.clavin.util.DamerauLevenshtein;
import com.berico.clavin.util.GeonamesUtils;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;

public class LuceneUtils {

	public static List<ResolvedCoordinate> convertToCoordinate(
			CoordinateOccurrence<?> occurrence,
			IndexSearcher searcher,
			TopDocs results,
			LuceneComponents components) throws IOException {
		
		ArrayList<ResolvedCoordinate> resolvedCoordinates = 
				new ArrayList<ResolvedCoordinate>();
		
		SpatialContext spatialContext = components.getSpatialContext();
		
		if (results.scoreDocs.length > 0){
			
			LatLonPair center = occurrence.convertToLatLon();
			
			Point occurrencePoint = 
				spatialContext.makePoint(
					center.getLongitude(), center.getLatitude());
			
			for (int i = 0; i < results.scoreDocs.length; i++){
			
				Document doc = searcher.doc(results.scoreDocs[i].doc);
				
				Place record = 
					GeonamesUtils.parseFromGeoNamesRecord(
						doc.get(FieldConstants.GEONAME));
				
				String positionOfLocation = doc.get(FieldConstants.GEOMETRY);
				
				// TODO: Spatial4J supposedly has a ShapeReaderWriter implementation
				// that we should be using instead.
				@SuppressWarnings("deprecation")
				Point point = (Point) spatialContext.readShape(positionOfLocation);
				
				double distanceInDegrees = 
						spatialContext.getDistCalc().distance(point, occurrencePoint);
				
				double distanceInKm = DistanceUtils.degrees2Dist(
						distanceInDegrees, DistanceUtils.EARTH_MEAN_RADIUS_KM);
				
				double direction = calculateDirection(
					point.getX(), point.getY(), 
					occurrencePoint.getX(), occurrencePoint.getY());
				
				resolvedCoordinates.add(
					new ResolvedCoordinate(occurrence, record, 
						new Vector(distanceInKm, direction)));
			}
		}
		
		return resolvedCoordinates;
	}
	
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
	
	
	public static List<ResolvedLocation> convertToLocations(
			LocationOccurrence occurrence, 
			IndexSearcher searcher, 
			TopDocs results,
			boolean usingFuzzy) throws IOException {
		
		ArrayList<ResolvedLocation> locations = new ArrayList<ResolvedLocation>();
		
	 	if (results.scoreDocs.length > 0) {
	    		
	    		for (int i = 0; i < results.scoreDocs.length; i++) {
	    			
	    			Document doc = searcher.doc(results.scoreDocs[i].doc);
	    			
	    			ResolvedLocation location = convertToLocation(doc, occurrence, true);
	    		
	    			locations.add(location);
	    		}
	 	}
	 	
		return locations;
	}
	
	public static ResolvedLocation convertToLocation(
			Document document, LocationOccurrence location, boolean fuzzy){
		
		Place geoname = 
				GeonamesUtils.parseFromGeoNamesRecord(document.get(FieldConstants.GEONAME));
		
		String matchedName = document.get(FieldConstants.NAME);
		
		float confidence = 
			DamerauLevenshtein
				.damerauLevenshteinDistanceCaseInsensitive(
					location.getText(), matchedName);
		
		return new ResolvedLocation(matchedName, geoname, location, fuzzy, confidence);
	}
	
	
}
