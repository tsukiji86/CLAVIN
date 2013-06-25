package com.berico.clavin.extractor.coords;

import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.gazetteer.LatLon;

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
 * LatLonOccurrence.java
 * 
 *###################################################################*/

/**
 * Represents a CoordinateOccurrence in the form of a Latitude and Longitude (LatLon).
 */
public class LatLonOccurrence implements CoordinateOccurrence<LatLon> {

	protected long position;
	protected String text;
	protected LatLon value;
	
	/**
	 * For serialization purposes.
	 */
	public LatLonOccurrence(){}
	
	/**
	 * Instantiate using the matched text, its position in the document, and the
	 * LatLon value of the text.
	 * @param text Matched text in document.
	 * @param position Position in document.
	 * @param value LatLon value of the text.
	 */
	public LatLonOccurrence(String text, long position, LatLon value) {
		
		this.position = position;
		this.text = text;
		this.value = value;
	}

	/**
	 * Get the position in the document the coordinate was found.
	 * @return Position in document.
	 */
	@Override
	public long getPosition() {
		
		return position;
	}
	
	/**
	 * Get the text representing the coordinate that was extracted from the document.
	 * @return Extracted text.
	 */
	@Override
	public String getExtractedText() {
		
		return text;
	}

	/**
	 * Get the underlying coordinate system.
	 * @return Coordinate system.
	 */
	@Override
	public String getCoordinateSystem() {
		
		return "LatLon";
	}

	/**
	 * Get the actual coordinate value.
	 * @return LatLon value of the coordinate.
	 */
	@Override
	public LatLon getValue() {
		
		return value;
	}

	/**
	 * Convert the value to it's LatLon representation (hey, it's already a LatLon!).
	 * @return LatLon value of the coordinate.
	 */
	@Override
	public LatLon convertToLatLon() {
		
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (position ^ (position >>> 32));
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		LatLonOccurrence other = (LatLonOccurrence) obj;
		if (position != other.position)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("LatLonOccurrence: \n");
		sb.append("\tText: ").append(this.text).append("\n");
		sb.append("\tPosition: ").append(this.position).append("\n");
		sb.append("\tValue: ").append(this.value).append("\n");
		
		return sb.toString();
	}
}
