package com.berico.clavin.examples;

import com.berico.clavin.gazetteer.LatLon;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.io.GeohashUtils;
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
 * GeoHash.java
 * 
 *###################################################################*/

/**
 * Represents a GeoHash, as defined by {@linkplain http://geohash.org/}.
 */
public class GeoHash {

	private String hashValue;
	private LatLon latlonValue;
	
	/**
	 * For serialization purposes.
	 */
	public GeoHash(){}
	
	/**
	 * Initialize with the GeoHash value.
	 * @param hashValue GeoHash value.
	 */
	public GeoHash(String hashValue){
		
		this.hashValue = hashValue;
	}

	/**
	 * Get the GeoHash Value.
	 * @return GeoHash Value.
	 */
	public String getHashValue() {
		return hashValue;
	}
	
	/**
	 * Get the LatLon Value of the GeoHash.
	 * @return LatLon value.
	 */
	public LatLon getLatLonValue(){
		
		if (latlonValue == null){
			
			// Spatial4J's Geohashing functionality.
			Point p = GeohashUtils.decode(this.hashValue, SpatialContext.GEO);
		
			latlonValue = new LatLon(p.getY(), p.getX());
		}
		
		return latlonValue;
	}
	
}
