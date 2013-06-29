package com.berico.clavin.resolver.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.clavin.resolver.LocationResolver;
import com.berico.clavin.resolver.ResolvedLocation;
import com.berico.clavin.resolver.impl.lucene.integration.LuceneLocationResolverIT;

public abstract class ResolverHeuristicsIT {

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
	protected int HAVERHILL_MA = 4939085;
	protected int WORCESTER_MA = 4956184;
	protected int SPRINGFIELD_MA = 4951788;
	protected int LEOMINSTER_MA = 4941873;
	protected int CHICAGO_IL = 4887398;
	protected int ROCKFORD_IL = 4907959;
	protected int SPRINGFIELD_IL = 4250542;
	protected int DECATUR_IL = 4236895;
	protected int KANSAS_CITY_MO = 4393217;
	protected int SPRINGFIELD_MO = 4409896;
	protected int ST_LOUIS_MO = 6955119;
	protected int INDEPENDENCE_MO = 4391812;
	protected int LONDON_UK = 2643743;
	protected int MANCHESTER_UK = 2643123;
	protected int HAVERHILL_UK = 2647310;
	protected int WORCESTER_UK = 2633560;
	protected int RESTON_VA = 4781530;
	protected int STRAßENHAUS_DE = 2826158;
	protected int GUN_BARREL_CITY_TX = 4695535;
	protected int TORONTO_ON = 6167865;
	protected int OTTAWA_ON = 6094817;
	protected int HAMILTON_ON = 5969782;
	protected int KITCHENER_ON = 5992996;
	protected int LONDON_ON = 6058560;

	/**
	 * Ensure we select the correct Springfield in a document about
	 * Massachusetts using context-based heuristic matching.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHeuristicsMassachusetts() throws Exception {
		String[] locations = {"Boston", "Haverhill", "Worcester", "Springfield", "Leominister"};
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(
				LuceneLocationResolverIT.makeOccurrencesFromNames(locations)).getLocations();
		
		assertEquals("LocationResolver chose the wrong \"Boston\"", BOSTON_MA, resolvedLocations.get(0).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Haverhill\"", HAVERHILL_MA, resolvedLocations.get(1).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Worcester\"", WORCESTER_MA, resolvedLocations.get(2).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_MA, resolvedLocations.get(3).getPlace().getId());
	}
	
	/**
	 * Ensure we select the correct Springfield in a document about
	 * Illinois using context-based heuristic matching.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHeuristicsIllinois() throws Exception {
		String[] locations = {"Chicago", "Rockford", "Springfield", "Decatur"};
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(
				LuceneLocationResolverIT.makeOccurrencesFromNames(locations)).getLocations();
	    
		assertEquals("LocationResolver chose the wrong \"Chicago\"", CHICAGO_IL, resolvedLocations.get(0).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Rockford\"", ROCKFORD_IL, resolvedLocations.get(1).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_IL, resolvedLocations.get(2).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Decatur\"", DECATUR_IL, resolvedLocations.get(3).getPlace().getId());
	}
	
	/**
	 * Ensure we select the correct Springfield in a document about
	 * Missouri using context-based heuristic matching.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHeuristicsMissouri() throws Exception {	    
		String[] locations = {"Kansas City", "Springfield", "St. Louis", "Independence"};
		
		List<ResolvedLocation> resolvedLocations = 
				getResolver().resolveLocations
				(LuceneLocationResolverIT.makeOccurrencesFromNames(locations)).getLocations();
	    
		assertEquals("LocationResolver chose the wrong \"Kansas City\"", KANSAS_CITY_MO, resolvedLocations.get(0).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_MO, resolvedLocations.get(1).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"St. Louis\"", ST_LOUIS_MO, resolvedLocations.get(2).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Independence\"", INDEPENDENCE_MO, resolvedLocations.get(3).getPlace().getId());
	}
	
	/**
	 * Ensure we select the correct Haverhill in a document about
	 * England using context-based heuristic matching.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHeuristicsEngland() throws Exception {	 		
		String[] locations = {"London", "Manchester", "Haverhill"};
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(
				LuceneLocationResolverIT.makeOccurrencesFromNames(locations)).getLocations();
	    
		assertEquals("LocationResolver chose the wrong \"London\"", LONDON_UK, resolvedLocations.get(0).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Manchester\"", MANCHESTER_UK, resolvedLocations.get(1).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Haverhill\"", HAVERHILL_UK, resolvedLocations.get(2).getPlace().getId());
	}
	
	/**
	 * Ensure we select the correct London in a document about
	 * Ontario using context-based heuristic matching.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHeuristicsOntario() throws Exception {	 		
		String[] locations = {"Toronto", "Ottawa", "Hamilton", "Kitchener", "London"};
		
		List<ResolvedLocation> resolvedLocations = getResolver().resolveLocations(
				LuceneLocationResolverIT.makeOccurrencesFromNames(locations)).getLocations();
	    
	    assertEquals("LocationResolver chose the wrong \"Toronto\"", TORONTO_ON, resolvedLocations.get(0).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Ottawa\"", OTTAWA_ON, resolvedLocations.get(1).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Hamilton\"", HAMILTON_ON, resolvedLocations.get(2).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"Kitchener\"", KITCHENER_ON, resolvedLocations.get(3).getPlace().getId());
		assertEquals("LocationResolver chose the wrong \"London\"", LONDON_ON, resolvedLocations.get(4).getPlace().getId());
	}

}
