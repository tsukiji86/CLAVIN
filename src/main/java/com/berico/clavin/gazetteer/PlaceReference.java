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
 * PlaceReference.java
 * 
 *###################################################################*/

/**
 * Represents a reference to another Place.  This is used
 * in the "Place" class to refer to super locales that may
 * be administrative (state, country) or geographic (continent) in nature. 
 * 
 * The assumption is that this Place is also in the Gazetteer.
 */
public class PlaceReference {

	protected String id;
	
	protected String name;
	
	protected boolean isAdministrativeParent = false;
	
	/**
	 * For serialization purposes.
	 */
	public PlaceReference(){}
	
	/**
	 * Instantiate the Place reference with the Place's id and name.
	 * @param id ID of the Place (found in the gazetteer).
	 * @param name Name of the Place.
	 */
	public PlaceReference(String id, String name) {
		
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Instantiate the Place reference with the Place's id and name.
	 * @param id ID of the Place (found in the gazetteer).
	 * @param name Name of the Place.
	 * @param isAdministrativeParent Is this the administrative parent location of the
	 * Place this reference is stored in?
	 */
	public PlaceReference(String id, String name, boolean isAdministrativeParent) {
		
		this.id = id;
		this.name = name;
		this.isAdministrativeParent = isAdministrativeParent;
	}

	/**
	 * Get the id of the reference.
	 * @return ID of this place (should be in the gazetteer).
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id of the reference.
	 * @param id ID of this place.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the name of the place.
	 * @return Name of the place this reference represents.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the place.
	 * @param name Name of the place this reference represents.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Is this the administrative parent of the Place in which this reference is contained?
	 * @return It is the admin parent if true.
	 */
	public boolean isAdministrativeParent() {
		return isAdministrativeParent;
	}

	/**
	 * Set whether this is the administrative parent of the Place in which is was
	 * referenced.
	 * @param isAdministrativeParent True if administrative parent.
	 */
	public void setAdministrativeParent(boolean isAdministrativeParent) {
		this.isAdministrativeParent = isAdministrativeParent;
	}
	
	@Override
	public String toString() {
		return String.format(
			"PlaceReference [id=%s, name=%s, isAdminParent=%s]", 
			id, name, isAdministrativeParent);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Equality is based solely on ID for this class.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceReference other = (PlaceReference) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}