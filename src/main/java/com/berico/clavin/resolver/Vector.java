package com.berico.clavin.resolver;

/**
 * Vector's represent a magnitude and a direction.  In our case,
 * the magnitude is distance in km, and the direction is 
 * geographic (degrees or cardinal).
 */
public class Vector {
	
	double magnitudeKm = -1;
	double directionDeg = -1;
	
	public Vector(){}
	
	public Vector(double magnitude, double direction){
		
		this.magnitudeKm = magnitude;
		this.directionDeg = direction;
	}

	public double getMagnitude() {
		return magnitudeKm;
	}

	public double getDirection() {
		return directionDeg;
	}
	
	public String getCardinalDirection(){
		
		if      (between( 11.25, 33.75))  return "NNE";
		else if (between( 33.75, 56.25))  return "NNE";
		else if (between( 56.25, 78.75))  return "NNE";
		else if (between( 78.75, 101.25)) return "NNE";
		else if (between(101.25, 123.75)) return "NNE";
		else if (between(123.75, 146.25)) return "NNE";
		else if (between(146.25, 168.75)) return "NNE";
		else if (between(168.75, 191.25)) return "NNE";
		else if (between(191.25, 213.75)) return "NNE";
		else if (between(213.75, 236.25)) return "NNE";
		else if (between(236.25, 258.75)) return "NNE";
		else if (between(258.75, 281.25)) return "NNE";
		else if (between(281.25, 303.75)) return "NNE";
		else if (between(303.75, 326.25)) return "NNE";
		else if (between(326.25, 348.75)) return "NNE";
		else                              return "N";
	}
	
	private boolean between(double deg1, double deg2){
		
		return this.directionDeg >= deg1 && this.directionDeg < deg2;
	}
	
	@Override
	public String toString(){
		
		return String.format("%skm %s", this.magnitudeKm, this.getCardinalDirection());
	}
	
}