package com.berico.clavin.integration.perf;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.resolver.ResolvedLocation;

public class IndexPerfTest {
	
	private static final String TINY_FILE = "src/test/resources/sample-docs/Tornado.txt";
	private static final String SMALL_FILE = "src/test/resources/sample-docs/IraqViolence.txt";
	private static final String MEDIUM_FILE = "src/test/resources/sample-docs/Somalia-doc.txt";
	private static final String LARGE_FILE = "src/test/resources/sample-docs/CathedralPeterborough.txt";
	private static final String MEGA_FILE = "src/test/resources/sample-docs/HistoryOfTioga.txt";
	
	
	GeoParser geoparser;
	
	@Before
	public void setup() throws Exception {
		
		geoparser = GeoParserFactory.getDefault("./IndexDirectory");
	}
	
	@Test
	public void tiny_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(TINY_FILE);
		
		System.out.println("Tiny File: " + results);
	}
	
	@Test
	public void small_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(SMALL_FILE);
		
		System.out.println("Small File: " + results);
	}
	
	@Test
	public void medium_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(MEDIUM_FILE);
		
		System.out.println("Medium File: " + results);
	}
	
	@Test
	public void large_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(LARGE_FILE);
		
		System.out.println("Large File: " + results);
	}
	
	@Test
	public void mega_file_test() throws Exception {
		
		PerformanceResults results = runPerformanceTest(MEGA_FILE);
		
		System.out.println("Mega File: " + results);
	}
	
	protected PerformanceResults runPerformanceTest(String file) throws Exception {
		
		String testDocument = FileUtils.readFileToString(new File(file));
		
		long start = System.nanoTime();
		
		List<ResolvedLocation> locations = geoparser.parse(testDocument);
		
		long end = System.nanoTime();
		
		long duration = end - start;
		
		return new PerformanceResults(duration, testDocument.length(), locations);
	}
	
	public static class PerformanceResults {
		
		public PerformanceResults(
			double duration, int textSize, List<ResolvedLocation> locations){
			
			this.duration = duration;
			this.textSize = textSize;
			this.locations = locations;
		}
		
		public double duration;
		public int textSize;
		public List<ResolvedLocation> locations;
		
		@Override
		public String toString(){
			
			return String.format(
				"%s locations parsed in %s seconds from %s characters of text", 
				locations.size(), duration / 1000000000l, textSize);
		}
	}
	
}
