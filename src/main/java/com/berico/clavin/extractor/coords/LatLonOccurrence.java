package com.berico.clavin.extractor.coords;

import com.berico.clavin.extractor.CoordinateOccurrence;

public class LatLonOccurrence implements CoordinateOccurrence<LatLonPair> {

	protected long position;
	protected String text;
	protected LatLonPair value;
	
	public LatLonOccurrence(){}
	
	public LatLonOccurrence(String text, long position, LatLonPair value) {
		
		this.position = position;
		this.text = text;
		this.value = value;
	}

	@Override
	public long getPosition() {
		
		return position;
	}

	@Override
	public String getExtractedText() {
		
		return text;
	}

	@Override
	public String getCoordinateSystem() {
		
		return "LatLon";
	}

	@Override
	public LatLonPair getValue() {
		
		return value;
	}

	@Override
	public LatLonPair convertToLatLon() {
		
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
