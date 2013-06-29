package com.berico.clavin.resolver;

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
 * Vector.java
 * 
 *###################################################################*/

/**
 * Vector's represent a magnitude and a direction.  In our case,
 * the magnitude is distance in km, and the direction is 
 * geographic (degrees or cardinal).
 */
public class Vector {
	
	double magnitudeKm = -1;
	double directionDeg = -1;
	
	/**
	 * For serialization purposes.
	 */
	public Vector(){}
	
	/**
	 * Initialize the vector with it's magnitude and direction.
	 * @param magnitude Distance in KM from target.
	 * @param direction Direction (degrees) from target.
	 */
	public Vector(double magnitude, double direction){
		
		this.magnitudeKm = magnitude;
		this.directionDeg = direction;
	}

	/**
	 * Get the distance (km) from the target.
	 * @return Distance in KM
	 */
	public double getMagnitude() {
		return magnitudeKm;
	}

	/**
	 * Get the direction (deg) from the target.
	 * @return Direction in degrees
	 */
	public double getDirection() {
		return directionDeg;
	}
	
	/**
	 * Get the cardinal direction (N|S|E|W)
	 * @return 16 increment cardinal direction
	 */
	public String getCardinalDirection(){
		
		if      (between( 11.25, 33.75))  return "NNE";
		else if (between( 33.75, 56.25))  return "NE";
		else if (between( 56.25, 78.75))  return "ENE";
		else if (between( 78.75, 101.25)) return "E";
		else if (between(101.25, 123.75)) return "ESE";
		else if (between(123.75, 146.25)) return "SE";
		else if (between(146.25, 168.75)) return "SSE";
		else if (between(168.75, 191.25)) return "S";
		else if (between(191.25, 213.75)) return "SSW";
		else if (between(213.75, 236.25)) return "SW";
		else if (between(236.25, 258.75)) return "WSW";
		else if (between(258.75, 281.25)) return "W";
		else if (between(281.25, 303.75)) return "WNW";
		else if (between(303.75, 326.25)) return "NW";
		else if (between(326.25, 348.75)) return "NNW";
		else                              return "N";
	}
	
	/**
	 * Is the direction in between the specified points?
	 * @param deg1 Degree 1
	 * @param deg2 Degree 2
	 * @return true if between
	 */
	private boolean between(double deg1, double deg2){
		
		return this.directionDeg >= deg1 && this.directionDeg < deg2;
	}
	
	@Override
	public String toString(){
		
		return String.format("%skm %s", this.magnitudeKm, this.getCardinalDirection());
	}
	
}