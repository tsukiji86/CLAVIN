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
	
	public PlaceReference(){}
	
	public PlaceReference(String id, String name) {
		
		this.id = id;
		this.name = name;
	}
	
	public PlaceReference(String id, String name, boolean isAdministrativeParent) {
		
		this.id = id;
		this.name = name;
		this.isAdministrativeParent = isAdministrativeParent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isAdministrativeParent() {
		return isAdministrativeParent;
	}

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