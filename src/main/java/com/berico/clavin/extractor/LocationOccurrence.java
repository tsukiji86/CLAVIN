package com.berico.clavin.extractor;

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
 * LocationOccurrence.java
 *
 *###################################################################*/

/**
 * Container class representing a location name found in a document.
 * Stores the text of the location name itself, as well as its position
 * in the text in which it was found (measured in UTF-16 code points
 * from the start of the document).
 * 
 */
public class LocationOccurrence {
	
	
	// text of location name
    protected String text;
    
    // number of UTF-16 code points from the start of the document at
    // which the location name starts
    protected long position;

    /**
     * For serialization purposes; please use the parameterized constructor.
     */
    public LocationOccurrence(){}
    
    /**
     * Construction for {@link LocationOccurrence} class.
     * 
     * Represents a location name found in a document.
     * 
     * @param text		text of the location name
     * @param position	where it was found
     */
	public LocationOccurrence(String text, long position) {
		this.text = text;
		
		assert position >= 0;
		
		this.position = position;
	}

	/**
	 * Get the extracted location text.
	 * @return Extracted location
	 */
	public String getText() {
		return text;
	}

	/**
	 * Get the position in the document in which the text was extracted.
	 * @return Position in text of the extracted location.
	 */
	public long getPosition() {
		return position;
	}
	
	/**
	 * Get the ending position in the document of the extracted location.
	 * @return End position of the extracted location.
	 */
	public long getEndPosition(){
		
		return position + text.length();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (position ^ (position >>> 32));
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		LocationOccurrence other = (LocationOccurrence) obj;
		if (position != other.position)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

    @Override
    public String toString(){
    	
    		return String.format("LocationOccurrence: [%s] at position %s.", this.text, this.position);
    }
    

    
}