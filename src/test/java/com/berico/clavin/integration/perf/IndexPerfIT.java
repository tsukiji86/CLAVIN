package com.berico.clavin.integration.perf;

import java.io.File;

import org.apache.commons.io.FileUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.resolver.ResolutionContext;


/**
 * TODO: Figure out good performance targets for each run.
 */
public class IndexPerfIT {
	
	private static final String TINY_FILE = "src/test/resources/sample-docs/Tornado.txt";
	private static final String SMALL_FILE = "src/test/resources/sample-docs/IraqViolence.txt";
	private static final String MEDIUM_FILE = "src/test/resources/sample-docs/Somalia-doc.txt";
	private static final String LARGE_FILE = "src/test/resources/sample-docs/CathedralPeterborough.txt";
	private static final String MEGA_FILE = "src/test/resources/sample-docs/HistoryOfTioga.txt";
	
	private static final long TINY_GOAL_NANO = Long.MAX_VALUE;
	private static final long SMALL_GOAL_NANO = Long.MAX_VALUE;
	private static final long MEDIUM_GOAL_NANO = Long.MAX_VALUE;
	private static final long LARGE_GOAL_NANO = Long.MAX_VALUE;
	private static final long MEGA_GOAL_NANO = Long.MAX_VALUE;
	
	GeoParser geoparser;
	
	@Before
	public void setup() throws Exception {
		
		geoparser = GeoParserFactory.getDefault("./IndexDirectory", 1, 1, true);
	}
	
	@Test
	public void tiny_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(TINY_FILE);
		
		System.out.println("Tiny File: " + results);
		
		assertThat(results.duration, lessThan(TINY_GOAL_NANO));
	}
	
	@Test
	public void small_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(SMALL_FILE);
		
		System.out.println("Small File: " + results);
		
		assertThat(results.duration, lessThan(SMALL_GOAL_NANO));
	}
	
	@Test
	public void medium_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(MEDIUM_FILE);
		
		System.out.println("Medium File: " + results);
		
		assertThat(results.duration, lessThan(MEDIUM_GOAL_NANO));
	}
	
	@Test
	public void large_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(LARGE_FILE);
		
		System.out.println("Large File: " + results);
		
		assertThat(results.duration, lessThan(LARGE_GOAL_NANO));
	}
	
	@Test
	public void mega_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(MEGA_FILE);
		
		System.out.println("Mega File: " + results);
		
		assertThat(results.duration, lessThan(MEGA_GOAL_NANO));
	}
	
	protected PerformanceResults runPerformanceTest(String file) throws Exception {
		
		String testDocument = FileUtils.readFileToString(new File(file));
		
		long start = System.nanoTime();
		
		ResolutionContext results = geoparser.parse(testDocument);
		
		long end = System.nanoTime();
		
		long duration = end - start;
		
		return new PerformanceResults(duration, testDocument.length(), results);
	}
	
	public static class PerformanceResults {
		
		public PerformanceResults(
			long duration, int textSize, ResolutionContext results){
			
			this.duration = duration;
			this.textSize = textSize;
			this.results = results;
		}
		
		public long duration;
		public int textSize;
		public ResolutionContext results;
		
		@Override
		public String toString(){
			
			return String.format(
				"%s locations parsed in %s seconds from %s characters of text", 
				results.getLocations().size(), duration / 1000000000l, textSize);
		}
	}
	
}
