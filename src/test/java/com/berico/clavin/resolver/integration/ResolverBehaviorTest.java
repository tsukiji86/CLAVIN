package com.berico.clavin.resolver.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.resolver.LocationResolver;
import com.berico.clavin.resolver.ResolvedLocation;

public abstract class ResolverBehaviorTest {

	public Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Deriving classes must provide a resolver,
	 * to which this super class will run a battery of
	 * tests to determine the implementation's underlying
	 * accuracy.
	 * @return Resolver to evaluate.
	 */
	protected abstract LocationResolver getResolver();
	
	// expected geonameID numbers for given location names
	protected int BOSTON_MA = 4930956;
	protected int RESTON_VA = 4781530;
	protected int STRAßENHAUS_DE = 2826158;
	protected int GUN_BARREL_CITY_TX = 4695535;

    //this convenience method turns an array of location name strings into a list of occurrences with fake positions.
    //(useful for tests that don't care about position in the document)
    public static List<LocationOccurrence> makeOccurrencesFromNames(String[] locationNames) {
        List<LocationOccurrence> locations = new ArrayList<LocationOccurrence>(locationNames.length);
        for(int i = 0; i < locationNames.length; ++i ) {
            locations.add(new LocationOccurrence(locationNames[i], i));
        }
        return locations;
    }
	
	@Test
	public void resolver_correctly_matches_locations_by_name() throws Exception {
		String[] locationNames = {"Reston", "reston", "RESTON", "Рестон", "Straßenhaus"};

		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(makeOccurrencesFromNames(locationNames), true);
		
		assertNotNull("Null results list received from LocationResolver", resolvedLocations);
		assertFalse("Empty results list received from LocationResolver", resolvedLocations.isEmpty());
		assertTrue("LocationResolver choked/quit after first location", resolvedLocations.size() > 1);
		
		assertEquals("LocationResolver failed exact String match", RESTON_VA, resolvedLocations.get(0).getGeoname().geonameID);
		assertEquals("LocationResolver failed on all lowercase", RESTON_VA, resolvedLocations.get(1).getGeoname().geonameID);
		assertEquals("LocationResolver failed on all uppercase", RESTON_VA, resolvedLocations.get(2).getGeoname().geonameID);
		assertEquals("LocationResolver failed on alternate name", RESTON_VA, resolvedLocations.get(3).getGeoname().geonameID);
		assertEquals("LocationResolver failed on UTF8 chars", STRAßENHAUS_DE, resolvedLocations.get(4).getGeoname().geonameID);
	}
	
	@Test
	public void resolver_handles_empty_input_correctly() throws Exception{
		
		String[] noLocations = {};
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(makeOccurrencesFromNames(noLocations), true);
		
		assertNotNull("Null results list received from LocationResolver", resolvedLocations);
		assertTrue("Non-empty results from LocationResolver on empty input", resolvedLocations.isEmpty());
	}
	
	@Test
	public void resolver_handles_null_input_correctly() throws Exception{
		
		List<LocationOccurrence> nullLocationList = null;
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(nullLocationList, true);
		
		assertNotNull("Null results list received from LocationResolver", resolvedLocations);
		assertTrue("Non-empty results from LocationResolver on empty input", resolvedLocations.isEmpty());
	}
	
	@Test
	public void resolver_handles_traditionally_reserved_words_patterns_and_unescaped_characters() 
			throws Exception {
		
		String[] locations = {"OR", "IN", "A + B", "A+B", "A +B", "A+ B", "A OR B", "A IN B", "A / B", "A \\ B",
				"Dallas/Fort Worth Airport", "New Delhi/Chennai", "Falkland ] Islands", "Baima ] County",
				"MUSES \" City Hospital", "North \" Carolina State"};
		
		getResolver().resolveLocations(makeOccurrencesFromNames(locations), true);
		
		assertTrue(true);
		// if no exceptions are thrown, the test is assumed to have succeeded
	}
	
	@Test
	public void resolver_selects_the_correct_locations_when_using_fuzzy_matching() throws Exception {
		String[] locations = {"Bostonn", "Reston12", "Bostn", "Straßenha", "Straßenhaus Airport", "Gun Barrel"};
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(makeOccurrencesFromNames(locations), true);
		
		assertEquals("LocationResolver failed on extra char", BOSTON_MA, resolvedLocations.get(0).getGeoname().geonameID);
		assertEquals("LocationResolver failed on extra chars", RESTON_VA, resolvedLocations.get(1).getGeoname().geonameID);
		assertEquals("LocationResolver failed on missing char", BOSTON_MA, resolvedLocations.get(2).getGeoname().geonameID);
		assertEquals("LocationResolver failed on missing chars", STRAßENHAUS_DE, resolvedLocations.get(3).getGeoname().geonameID);
		assertEquals("LocationResolver failed on extra term", STRAßENHAUS_DE, resolvedLocations.get(4).getGeoname().geonameID);
		assertEquals("LocationResolver failed on missing term", GUN_BARREL_CITY_TX, resolvedLocations.get(5).getGeoname().geonameID);
	}

}
