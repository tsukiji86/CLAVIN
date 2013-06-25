package com.berico.clavin.gazetteer;

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
* LatLon.java
*
*###################################################################*/


/**
 * Represents a latitude/longitude pair.
 */
public class LatLon {

	/**
	 * Value that we will use to represent a null coordinate.
	 */
	public static final double NULL_VALUE = Double.MIN_VALUE;
	
	protected double latitude = NULL_VALUE;
	protected double longitude = NULL_VALUE;
	
	/**
	 * For serialization purposes.  Please use the parameterized
	 * constructor.
	 */
	public LatLon(){}
	
	/**
	 * Initialize a lat/lon pair with it's lat/lon!
	 * @param latitude Latitude
	 * @param longitude Longitude
	 */
	public LatLon(double latitude, double longitude){
		
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	/**
	 * Get the latitude of the coordinate.
	 * @return Latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Set the latitude of the coordinate.  Ensure "-90 &gt;= latitude &lt;= 90";
	 * an assertion is used to guard the setter, but you could have your assertions
	 * turned off!
	 * @param latitude Latitude
	 */
	public void setLatitude(double latitude) {
		
		assert latitude >= -90 && latitude <= 90;
		
		this.latitude = latitude;
	}

	/**
	 * Get the longitude of the coordinate.
	 * @return Longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Set the longitude of the coordinate.  Ensure "-180 &gt;= longitude &lt;= 180";
	 * an assertion is used to guard the setter, but you could have your assertions
	 * turned off!
	 * @param longitude Longitude
	 */
	public void setLongitude(double longitude) {
		
		assert longitude >= -180 && longitude <= 180;
		
		this.longitude = longitude;
	}

	/**
	 * Is this coordinate null?
	 * @return true if null.
	 */
	public boolean isNull(){
		
		return this.latitude == NULL_VALUE|| this.longitude == NULL_VALUE;
	}
	
	/**
	 * Is this coordinate NOT null?
	 * @return true is not null
	 */
	public boolean isntNull(){
		
		return !isNull();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LatLon other = (LatLon) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		
		if (isNull())
			return "LatLon is effectively NULL.";
		
		return String.format("%s, %s", this.latitude, this.longitude);
	}
	
}
