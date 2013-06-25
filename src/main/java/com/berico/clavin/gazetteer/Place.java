package com.berico.clavin.gazetteer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


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
 * Place.java
 * 
 *###################################################################*/

/**
 * Data-rich representation of a named location.
 * 
 * NOTES: This used to be "GeoName" but, that didn't make much 
 * sense if we want to support other Gazetteers, so it has been
 * renamed to "Place".
 * 
 * Despite the migration away from a geonames specific place, this
 * model is still heavily inspired by Geonames.  In fact, we are
 * retaining the Feature Class and Code ontologies since they are
 * incredibly useful for disambiguation purposes.
 * 
 */
public class Place {
	
	// sentinel value used in place of null when numeric value in
	// GeoNames record is not provided (see: geonameID, latitude,
	// longitude, population, elevation, digitalElevationModel)
	public static final int OUT_OF_BOUNDS = -9999999;
	
	// id of record in geonames database
	protected int id;
	
	// name of geographical point (utf8)
	protected String name;
	
	// name of geographical point in plain ascii characters
	protected String asciiName;
	
	// list of alternate names for location
	protected List<String> alternateNames;
	
	// The lat/lon center of this place
	protected LatLon center;
	
	//TODO: put actual shape context here.
	// protected Shape extent;
	
	// major feature category
	// (see http://www.geonames.org/export/codes.html)
	protected FeatureClass featureClass;
	
	// http://www.geonames.org/export/codes.html
	protected FeatureCode featureCode;
	
	// ISO-3166 2-letter country code
	protected CountryCode primaryCountryCode;
	
	// list of alternate ISO-3166 2-letter country codes
	protected List<CountryCode> alternateCountryCodes;
	
	// Arbitrary set of super places this place exists inside of.
	protected List<PlaceReference> superPlaces = new ArrayList<PlaceReference>();
	
	// total number of inhabitants
	protected long population;
	
	// in meters
	protected double elevation;
	
	// timezone for geographical point
	protected TimeZone timezone;
	
	// date of last modification in GeoNames database
	protected Date modificationDate;
	
	// field storing extra data about this Place (may be specific to the gazetteer).
	protected String context;
	
	/**
	 * Generally for serializer access.
	 */
	public Place(){}
	
	/**
	 * Get the ID of this entry.
	 * @return ID of the entry.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the ID of this entry.  Needs to be unique, but the
	 * strategy used (UUID or hash) doesn't particularly matter.
	 * @param id of the entry.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the canonical (definitive) name of this place.
	 * @return The canonical name of this place.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the canonical (definitive) name of this place.
	 * @param name The canonical name of this place.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the ASCII (name is UTF-8) name of this place.
	 * @return ASCII name.
	 */
	public String getAsciiName() {
		return asciiName;
	}

	/**
	 * Set the ASCII name of this place.
	 * @param asciiName ASCII name.
	 */
	public void setAsciiName(String asciiName) {
		this.asciiName = asciiName;
	}

	/**
	 * Get the list of alternate names for this place.  For some places,
	 * this might be "slang names" (Big Apple) or historical names (Constantinople).
	 * @return Alternate Names
	 */
	public List<String> getAlternateNames() {
		return alternateNames;
	}

	/**
	 * Set the list of alternate names for this place.
	 * @param alternateNames List of alternate names.
	 */
	public void setAlternateNames(List<String> alternateNames) {
		this.alternateNames = alternateNames;
	}

	/**
	 * Get the center coordinate of this place.  This is a Latitude Longitude
	 * pair.
	 * @return LatLonPair of the center of this place.
	 */
	public LatLon getCenter() {
		return center;
	}

	/**
	 * Set the center coordinate of this place.
	 * @param center LatLonPair representing the center.
	 */
	public void setCenter(LatLon center) {
		this.center = center;
	}

	/**
	 * Get the feature class of this place.
	 * @return Feature Class.
	 */
	public FeatureClass getFeatureClass() {
		return featureClass;
	}

	/**
	 * Set the feature class of this place.
	 * @param featureClass Feature Class.
	 */
	public void setFeatureClass(FeatureClass featureClass) {
		this.featureClass = featureClass;
	}

	/**
	 * Get the feature code of this place.
	 * @return Feature Code.
	 */
	public FeatureCode getFeatureCode() {
		return featureCode;
	}

	/**
	 * Set the feature code of this place.
	 * @param featureCode Feature Code.
	 */
	public void setFeatureCode(FeatureCode featureCode) {
		this.featureCode = featureCode;
	}

	/**
	 * Get the country code of this place.  This object
	 * also holds the String representation of the 
	 * country name.
	 * @return Country Code
	 */
	public CountryCode getPrimaryCountryCode() {
		return primaryCountryCode;
	}

	/**
	 * Set the primary country (code) of this place.
	 * @param primaryCountryCode Country Code representation.
	 */
	public void setPrimaryCountryCode(CountryCode primaryCountryCode) {
		this.primaryCountryCode = primaryCountryCode;
	}

	/**
	 * Get alternate country codes for this place.  This is common
	 * for places in areas with contested borders.
	 * @return Alternate Country Codes
	 */
	public List<CountryCode> getAlternateCountryCodes() {
		return alternateCountryCodes;
	}

	/**
	 * Set the alternate country codes for this place.
	 * @param alternateCountryCodes Alternate Country Codes
	 */
	public void setAlternateCountryCodes(List<CountryCode> alternateCountryCodes) {
		this.alternateCountryCodes = alternateCountryCodes;
	}

	/**
	 * Get the parent or "super" places for this place.  A "super place" is a place in
	 * which this place exists inside of.  For instance, North America is a super place
	 * of Texas.
	 * @return List of Super Places.
	 */
	public List<PlaceReference> getSuperPlaces() {
		return superPlaces;
	}

	/**
	 * Set the parent or "super" places for this place.
	 * @param superPlaces  List of super places.
	 */
	public void setSuperPlaces(List<PlaceReference> superPlaces) {
		this.superPlaces = superPlaces;
	}
	
	/**
	 * Get the administrative parents (state, country, etc) for this place.
	 * @return A list of administrative parents.
	 */
	public List<PlaceReference> getAdministrativeParents(){
		
		ArrayList<PlaceReference> aps = new ArrayList<PlaceReference>();
		
		for (PlaceReference pr : this.superPlaces){
			
			if (pr.isAdministrativeParent){
				
				aps.add(pr);
			}
		}
		
		return aps;
	}

	/**
	 * Get the population of this place.
	 * @return population
	 */
	public long getPopulation() {
		return population;
	}

	/**
	 * Set the population of this place.
	 * @param population Population
	 */
	public void setPopulation(long population) {
		this.population = population;
	}

	/**
	 * Get the elevation (in meters) of this place.
	 * @return Elevation in meters
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * Set the elevation (in meters) of this place.
	 * @param elevation Elevation in meters
 	 */
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	/**
	 * Get the timezone of this place.
	 * @return timezone
	 */
	public TimeZone getTimezone() {
		return timezone;
	}

	/**
	 * Set the timezone of this place.
	 * @param timezone timezone
	 */
	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}

	/**
	 * Get the last time this entry was modified.
	 * @return modification date
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * Set the last time this entry was modified.
	 * @param modificationDate modification date
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * Get the contextual (serialized) representation of this place.
	 * This is useful if you have more context you want to retrieve from
	 * the place, but the schema of the index doesn't support (or care) those
	 * attributes.
	 * @return context of the place
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Set the contextual (serialized) representation of this place.
	 * This is a great way to append attributes you want added to this place,
	 * that are not essential to the resolution process.
	 * @param context context to add to this place.
	 */
	public void setContext(String context) {
		this.context = context;
	}
	
	@Override
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Place [")
		  .append("name: ").append(this.name)
		  .append(", country: ").append(this.primaryCountryCode.name())
		  .append(", center: ").append(this.center)
		  .append("]");
		
		return sb.toString();
	}
}
