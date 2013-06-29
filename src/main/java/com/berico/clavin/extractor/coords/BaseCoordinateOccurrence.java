package com.berico.clavin.extractor.coords;

import com.berico.clavin.extractor.CoordinateOccurrence;

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
 * Provides the boilerplate code you will probably need to implement when adding
 * a Coordinate system.
 * @param <T> Coordinate type, to be defined in derived classes.
 */
public abstract class BaseCoordinateOccurrence<T> implements CoordinateOccurrence<T> {

	protected long position;
	protected String text;
	protected T value;
	
	/**
	 * For serialization purposes only.
	 */
	public BaseCoordinateOccurrence(){}
	
	/**
	 * Initialize the Occurrence with the necessary context.
	 * @param position Position coordinate occurred in the context.
	 * @param text Extracted coordinate text from the document.
	 * @param value Value of the coordinate.
	 */
	public BaseCoordinateOccurrence(long position, String text, T value) {
		
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
	 * Get the actual coordinate value.
	 * @return Value of the coordinate.
	 */
	@Override
	public T getValue() {
		
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
		BaseCoordinateOccurrence other = (BaseCoordinateOccurrence) obj;
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
		
		sb.append(getClass().getName()).append(": \n");
		sb.append("\tText: ").append(this.text).append("\n");
		sb.append("\tPosition: ").append(this.position).append("\n");
		sb.append("\tValue: ").append(this.value).append("\n");
		
		return sb.toString();
	}
}
