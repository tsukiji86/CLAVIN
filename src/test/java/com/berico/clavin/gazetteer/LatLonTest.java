package com.berico.clavin.gazetteer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LatLonTest {

	@Test
	public void ensure_equals_evaluates_correctly() {
		
		LatLon ll1 = new LatLon(1, 2);
		
		LatLon ll2 = new LatLon(1, 2);
		
		LatLon ll3 = new LatLon(1.0, 2.0);
		
		assertEquals(ll1, ll2);
		
		assertEquals(ll2, ll3);
		
		assertEquals(ll1, ll3);
	}

	@Test
	public void isNull_is_true_when_LatLon_has_no_values(){
		
		LatLon shouldBeNull = new LatLon();
		
		assertTrue(shouldBeNull.isNull());
		
		assertFalse(shouldBeNull.isntNull());
	}
	
	@Test
	public void isNull_is_false_when_LatLon_has_values(){
		
		LatLon shouldNotBeNull = new LatLon(1, 2);
		
		assertFalse(shouldNotBeNull.isNull());
		
		assertTrue(shouldNotBeNull.isntNull());
	}
	
	@Test(expected=AssertionError.class)
	public void setting_too_low_of_a_latitude_value_results_in_error(){
		
		LatLon ll = new LatLon();
		
		ll.setLatitude(-90.1);
	}
	
	@Test(expected=AssertionError.class)
	public void setting_too_high_of_a_latitude_value_results_in_error(){
		
		LatLon ll = new LatLon();
		
		ll.setLatitude(90.1);
	}
	
	@Test(expected=AssertionError.class)
	public void setting_too_low_of_a_longitude_value_results_in_error(){
		
		LatLon ll = new LatLon();
		
		ll.setLongitude(-180.1);
	}
	
	@Test(expected=AssertionError.class)
	public void setting_too_high_of_a_longitude_value_results_in_error(){
		
		LatLon ll = new LatLon();
		
		ll.setLongitude(180.1);
	}
}
