package com.berico.clavin.util;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import static org.junit.Assert.*;

import com.berico.clavin.gazetteer.CountryCode;
import com.berico.clavin.gazetteer.FeatureClass;
import com.berico.clavin.gazetteer.FeatureCode;
import com.berico.clavin.gazetteer.LatLon;
import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.gazetteer.PlaceReference;

public class DefaultSerializerTest {
	
	@Test
	public void DefaultSerializer_correctly_serializes_and_deserializes_an_object() {
		
		Place expected = new Place();
		
		expected.setAlternateCountryCodes(Arrays.asList(CountryCode.AD, CountryCode.AE));
		expected.setAlternateNames(Arrays.asList("Test1", "Test2"));
		expected.setAsciiName("AsciiName with ' some # 4`~[]? |\\/)*^@\"<{} wierd characters.");
		expected.setCenter(new LatLon(1, 2));
		expected.setContext("Context");
		expected.setElevation(42);
		expected.setFeatureClass(FeatureClass.A);
		expected.setFeatureCode(FeatureCode.ADM1);
		expected.setId(1);
		expected.setModificationDate(new Date());
		expected.setName("Test Place");
		expected.setPopulation(100);
		expected.setPrimaryCountryCode(CountryCode.AF);
		expected.setSuperPlaces(Arrays.asList(new PlaceReference("2", "Parent", true)));
		expected.setTimezone(TimeZone.getAvailableIDs()[0]);
		
		String serializedOutput = Serializer.Default.serialize(expected);
		
		Place actual = Serializer.Default.deserialize(serializedOutput, Place.class);
		
		assertEquals(expected.getAdministrativeParents(), actual.getAdministrativeParents());
		assertEquals(expected.getAlternateCountryCodes(), actual.getAlternateCountryCodes());
		assertEquals(expected.getAlternateNames(), actual.getAlternateNames());
		assertEquals(expected.getAsciiName(), actual.getAsciiName());
		assertEquals(expected.getCenter(), actual.getCenter());
		assertEquals(expected.getContext(), actual.getContext());
		assertEquals(expected.getElevation(), actual.getElevation(), 0.1d);
		assertEquals(expected.getFeatureClass(), actual.getFeatureClass());
		assertEquals(expected.getFeatureCode(), actual.getFeatureCode());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getModificationDate(), actual.getModificationDate());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getPopulation(), actual.getPopulation());
		assertEquals(expected.getPrimaryCountryCode(), actual.getPrimaryCountryCode());
		assertEquals(expected.getSuperPlaces(), actual.getSuperPlaces());
		assertEquals(expected.getTimezone(), actual.getTimezone());
		assertEquals(expected.getTimezoneID(), actual.getTimezoneID());
		
		// Finally...
		assertEquals(expected, actual);
	}

}
