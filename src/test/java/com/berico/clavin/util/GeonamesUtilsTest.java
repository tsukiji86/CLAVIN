package com.berico.clavin.util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.berico.clavin.gazetteer.CountryCode;
import com.berico.clavin.gazetteer.FeatureClass;
import com.berico.clavin.gazetteer.FeatureCode;
import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.gazetteer.PlaceReference;

public class GeonamesUtilsTest {

	/**
	 * Parse a bunch of gazetteer records and make sure we're building
	 * the correct {@link Place} objects.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void parseFromGeoNamesRecord_correctly_parses_Place_objects_from_Geoname_records() 
			throws IOException, ParseException {
		
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
				new File("./src/test/resources/gazetteers/GeoNamesSampleSet.txt")), "UTF-8"));
		String line;
		ArrayList<Place> geonames = new ArrayList<Place>();
		while ((line = r.readLine()) != null)
			geonames.add(GeonamesUtils.parseFromGeoNamesRecord(line));
		
		Place place;
		place = geonames.get(0); // standard US city
		assertEquals("incorrect geonameID", 4781530, place.getId());
		assertEquals("incorrect name", "Reston", place.getName());
		assertEquals("incorrect asciiName", "Reston", place.getAsciiName());
		assertEquals("incorrect alternateNames", Arrays.asList("Reston","Рестон"), place.getAlternateNames());
		assertEquals("incorrect latitude", 38.96872, place.getCenter().getLatitude(), 0.1);
		assertEquals("incorrect longitude", -77.3411, place.getCenter().getLongitude(), 0.1);
		assertEquals("incorrect featureClass", FeatureClass.P, place.getFeatureClass());
		assertEquals("incorrect featureCode", FeatureCode.PPL, place.getFeatureCode());
		assertEquals("incorrect primaryCountryCode", CountryCode.US, place.getPrimaryCountryCode());
		assertEquals("incorrect primaryCountryName", CountryCode.US.name, place.getPrimaryCountryCode().name);
		assertEquals("incorrect alternateCountryCodes", new ArrayList<CountryCode>(), place.getAlternateCountryCodes());
		
		List<PlaceReference> adminParents = place.getAdministrativeParents();
		
		String a1 = adminParents.get(0).getName();
		String a2 = adminParents.get(1).getName();
		String a3 = adminParents.get(2).getName();
		String a4 = adminParents.get(3).getName();
		
		assertEquals("incorrect adminCode1", "VA", a1);
		assertEquals("incorrect adminCode2", "059", a2);
		assertEquals("incorrect adminCode3", "", a3);
		assertEquals("incorrect adminCode4", "", a4);
		assertEquals("incorrect population", 58404, place.getPopulation());
		assertEquals("incorrect elevation", 100.0, place.getElevation(), 1.0);
		assertEquals("incorrect timezone", TimeZone.getTimeZone("America/New_York"), place.getTimezone());
		assertEquals("incorrect modificationDate", new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-14"), place.getModificationDate());
		
		place = geonames.get(1); // lots of UTF chars & missing columns
		assertEquals("incorrect geonameID", 1139905, place.getId());
		assertEquals("incorrect name", "Ḩowẕ-e Ḩājī Bēg", place.getName());
		assertEquals("incorrect asciiName", "Howz-e Haji Beg", place.getAsciiName());
		assertEquals("incorrect alternateNames", Arrays.asList(
				"Hawdze Hajibeg","Howz-e Haji Beg",
				"Howz-e Hajjibeyg","H̱awdze Ḩājibeg",
				"حوض حاجی بېگ","Ḩowẕ-e Ḩājjībeyg",
				"Ḩowẕ-e Ḩājī Bēg"), place.getAlternateNames());
		assertEquals("incorrect latitude", 34.90489, place.getCenter().getLatitude(), 0.1);
		assertEquals("incorrect longitude", 64.10312, place.getCenter().getLongitude(), 0.1);
		
		place = geonames.get(2); // seldom-used fields
		assertEquals("incorrect geonameID", 2826158, place.getId());
		assertEquals("incorrect alternateNames", new ArrayList<String>(), place.getAlternateNames());
		
		// TODO: Figure out the geonames strategy for 3, 4 labels since we now support much longer
		// hierarchies.
		//assertEquals("incorrect adminCode3", "07138", geoname.admin3Code);
		//assertEquals("incorrect adminCode4", "07138071", geoname.admin4Code);
		
		place = geonames.get(3); // no primaryCountryCode
		assertEquals("incorrect primaryCountryCode", CountryCode.NULL, place.getPrimaryCountryCode());
		
		place = geonames.get(4); // non-empty alternateCountryCodes
		assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.US, CountryCode.MX), place.getAlternateCountryCodes());
		
		place = geonames.get(5); // malformed alternateCountryCodes
		assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.PS), place.getAlternateCountryCodes());
		
		place = geonames.get(6); // no featureCode
		assertEquals("incorrect featureClass", FeatureCode.NULL, place.getFeatureCode());
		
		r.close();
	}


}
