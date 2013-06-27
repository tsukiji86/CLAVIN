package com.berico.clavin.resolver.impl.lucene;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.spatial.SpatialStrategy;

import com.berico.clavin.gazetteer.Place;
import com.berico.clavin.util.Serializer;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Shape;

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
 * IndexBuilder.java
 * 
 *###################################################################*/

/**
 * This is an attempt to simplify developer's lives in extending the CLAVIN index
 * creation process.  Extend this class and implement your Gazetteer specific
 * logic and let us do the heavy lifting of configuring the index and 
 * writing the records to disk.
 */
public abstract class IndexBuilder implements BuilderContext {

	/**
	 * Provide a description for this particular implementation of the IndexBuilder.
	 * @return Description to show on the console.
	 */
	abstract String getDescription();

	/**
	 * Extend the argument parser with your own custom parameters.
	 * @param parser precreated argument parser.
	 */
	abstract void extend(ArgumentParser parser);

	/**
	 * Provide the parsed argument input to the derived class so it can configure
	 * itself.
	 * @param namespace Parsed argument input.
	 */
	abstract void initialize(Namespace namespace);
	
	/**
	 * Begin pulling Gazetteer entries from wherever, calling "addPlaceToIndex"
	 * to store them.
	 * @param context Context of index building process.
	 * @throws Exception Exceptions may be thrown at any point; the IndexBuilder
	 * will catch the Exception and call cleanup() so you can release resources.
	 */
	abstract void begin(BuilderContext context) throws Exception;
	
	/**
	 * Here's your chance to cleanup.
	 * @throws Exception you can throw an exception in cleanup, but it won't be
	 * caught.
	 */
	abstract void cleanup() throws Exception;
	
	/**
	 * Location of the index directory.
	 */
	protected String indexDirectory;
	
	/**
	 * Lucene Spatial Context
	 */
	protected SpatialContext spatialContext;
	
	/**
	 * Spatial Indexing Strategy
	 */
	protected SpatialStrategy spatialStrategy;
	
	/**
	 * Lucene Index Writer
	 */
	protected IndexWriter indexWriter;
	
	/**
	 * Total number of records processed.
	 */
	protected long totalNumberProcessed = 0l;
	
	/**
	 * Reusable index fields.
	 */
	private TextField indexNameField = new TextField(FieldConstants.NAME, "", Field.Store.YES);
	private StoredField geonameField = new StoredField(FieldConstants.PLACE, "");
	private IntField geonameIdField = new IntField(FieldConstants.PLACE_ID, -1, Field.Store.NO);
	private NumericDocValuesField populationField = new NumericDocValuesField(FieldConstants.POPULATION, -1l);
	private StoredField geospatialField = new StoredField(FieldConstants.GEOMETRY, "");
	
	/**
	 * Instantiate the IndexBuilder with the command line input.
	 * @param args
	 * @throws IOException
	 */
	public IndexBuilder(String[] args) throws Exception {

		// Give the user a nice warm welcome.
		printBanner();
		
		// Parse the input arguments.
		parseArguments(args);
		
		br(); 
		pl("Starting the Index Builder...");
		
		String absoluteIndexDir = new File(indexDirectory).getAbsolutePath();
		
		pl("> Writing index to: %s", absoluteIndexDir);
		pl("> Each dot represents 1,000 processed records.");
		
		pl("---------------------------------------------------------------------");
		pl("> Press Control-C (or unplug your computer) to terminate");
		pl("> the indexing process.");
		pl("---------------------------------------------------------------------");
		
		// Initialize the index.
		initializeIndex();
		
		try {
			
			// Stopwatch
			Date start = new Date();
			
			// Delegate processing to derived classes.
			begin(this);
			
			// Stop!
			Date end = new Date();
			
			// Calculate total time.
			long totalTime = end.getTime() - start.getTime();
			
			// Pretty print dates.
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			
			// Print elapsed time.
			pl("");
			pl("-----------------------------------------------------------");
			pl("Process started: %s, ended: %s; elasped time: %s seconds.", 
				df.format(start), df.format(end), totalTime / 1000);
			
			br();
			br();
		} 
		catch (Exception e){
			
			pl("An error occurred while building the index.");
			
			e.printStackTrace();
		}
		
		// Instruct the derived class to release resources and cleanup.
		cleanup();
	}

	/**
	 * Parse the command line arguments, retrieving configuration needed for
	 * index building.
	 * @param arguments command line input
	 */
	protected void parseArguments(String[] arguments) {

		ArgumentParser parser = 
			ArgumentParsers.newArgumentParser("clavin")
				.description(getDescription());

		// Register a variable called "index"
		parser.addArgument("index")
				.metavar("index-directory")
				.type(String.class)
				.required(true)
				.help("Index directory location (will create if it doesn't exist).");

		// Allow the derived class to extend the arguments parser.
		extend(parser);

		Namespace namespace = null;

		try {

			// Parse the input arguments
			namespace = parser.parseArgs(arguments);

			// Collect the index directory.
			indexDirectory = namespace.getString("index");
			
		} catch (ArgumentParserException ex) {

			parser.handleError(ex);

			System.exit(1);
		}

		// Delegate the collection of arguments to the derived class.
		initialize(namespace);
	}
	
	/**
	 * Initialize the Lucene index.
	 * @throws IOException
	 */
	protected void initializeIndex() throws IOException{
	
		final LuceneComponentsFactory factory = new LuceneComponentsFactory(indexDirectory);
		
		factory.initializeWriter();
		
		indexWriter = factory.getIndexWriter();
		
		spatialContext = factory.getSpatialContext();
		
		spatialStrategy = factory.getSpatialStrategy();
		
		// Register a shutdown hook to close the indexes when the process terminates.
		Runtime.getRuntime().addShutdownHook(new Thread(){
			
			@Override
			public void run(){
				
				try {
					
					factory.getIndexWriter().close();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				factory.getIndex().close();
			}
		});
	}
	
	/**
	 * Add a place to the index.  This is a method of the BuilderContext
	 * implementation.
	 * @param place Place to add to the index.
	 */
	public void add(Place place){
		
		try {
		
			addPlaceToIndex(place);
			
			incrementProcessCounter();
		}
		catch (Exception ex){
			
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Increment the total processed counter and print a message
	 * if a threshold is reached.
	 */
	protected void incrementProcessCounter(){
		
		totalNumberProcessed++;
		
		if (totalNumberProcessed % 1000 == 0) p(".");
	}
	
	/**
	 * Get the total number of records processed.
	 * @return total processed.
	 */
	public long getTotalProcessed(){
		
		return totalNumberProcessed;
	}
	
	/**
	 * Call this to add a place to the index.
	 * @param place Place to add.
	 * @throws IOException
	 */
	public void addPlaceToIndex(Place place) throws IOException{
		
		Document document = buildDocument(place);
		
		indexWriter.addDocument(document);
	}
	
	
	/**
  	 * Builds a Lucene document to be added to the index based on a
  	 * specified name for the location and the corresponding
  	 * {@link Place} object.
  	 * 
  	 * @param name			name to serve as index key
  	 * @param place		GeoName Entry
  	 * @return
  	 */
  	private Document buildDocument(Place place) {
  		
  		// in case you're wondering, yes, this is a non-standard use of
  		// the Lucene Document construct
	    Document doc = new Document();
	    
	    // this is essentially the key we'll try to match location
	    // names against
	    addIndexNameField(doc, place.getName());
	    
	    if (!place.getAsciiName().equals(place.getName())){
	    	
	    		addIndexNameField(doc, place.getAsciiName());
	    }
	    
	    for (String altName : place.getAlternateNames()){
	    	
	    		if (!altName.equals(place.getName()) && !altName.equals(place.getAsciiName())){
	    	
	    			addIndexNameField(doc, altName);
	    		}
	    }
	    
	    // this is the payload we'll return when matching location
	    // names to gazetteer records
	    addGeonameField(doc, Serializer.Default.serialize(place));
	    
	    // TODO: use geonameID to link administrative subdivisions to
	    //		 each other
	    addGeonameIdField(doc, place.getId());
	    
	    // we'll initially sort match results based on population
	    addPopulationField(doc, place.getPopulation());
	    
	    // we'll create a new Spatial geometry from the centroid of the geoname location
	    Shape centroid = spatialContext.makePoint(
	    		place.getCenter().getLongitude(), place.getCenter().getLatitude());
	    
	    // add a deserializable representation of the shape to the document.
	    addGeospatialField(doc, centroid);
	    
	    // we will add the field to the index
	    for (Field f : spatialStrategy.createIndexableFields(centroid)){
	    		
	    		doc.add(f);
	    }
	    
	    return doc;
  	}
  	
  	private void addIndexNameField(Document doc, String value){
  		
  		indexNameField.setStringValue(value);
  		
  		doc.add(indexNameField);  		
  	}
  	
  	private void addGeonameField(Document doc, String geoname){
  		
  		geonameField.setStringValue(geoname);
  		
  		doc.add(geonameField);
  	}
  	
  	private void addGeonameIdField(Document doc, int geonameId){
  		
  		geonameIdField.setIntValue(geonameId);
  		
  		doc.add(geonameIdField);
  	}
  	
  	private void addPopulationField(Document doc, long population){
  		
  		populationField.setLongValue(population);
  		
  		doc.add(populationField);
  	}
  	
  	@SuppressWarnings("deprecation")
	private void addGeospatialField(Document doc, Shape shape){
  		
		// TODO: Maybe do this more elegantly with the Spatial4J API's
  		// ShapeReaderWriter...
  		geospatialField.setStringValue(spatialContext.toString(shape));
  		
  		doc.add(geospatialField);
  	}
	
	/**
	 * Print a message to the console.
	 * 
	 * @param message
	 *            Message to print.
	 */
	public static void p(String message) {

		System.out.print(message);
	}

	/**
	 * Print a message to the console, using a string formatter.
	 * 
	 * @param template
	 *            Template string
	 * @param objects
	 *            context
	 */
	public static void p(String template, Object... objects) {

		p(String.format(template, objects));
	}

	/**
	 * Print a message to the console on its own line.
	 * 
	 * @param message
	 *            Message to print.
	 */
	public static void pl(String message) {

		System.out.println(message);
	}

	/**
	 * Print a message to the console on its own line, using a string formatter.
	 * 
	 * @param template
	 *            Template string
	 * @param objects
	 *            context
	 */
	public static void pl(String template, Object... objects) {

		pl(String.format(template, objects));
	}
	
	/**
	 * Print a line return to the console.
	 */
	public static void br(){ p("\n"); }
	
	/**
	 * Print a nice CLAVIN Banner, hurray!
	 */
	public void printBanner(){
		
		// Get the current classloader.
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		// Retrieve the banner text from the text file in the src/main/resources
		// directory.
		InputStream is = cl.getResourceAsStream("banner.txt");
		
		try {
			
			// Pull the banner from the input stream
			String banner = IOUtils.toString(is);
			
			// Print the banner.
			p(banner);
			
		} catch (IOException e) {
			
			// Oops, didn't work!
			e.printStackTrace();
		}
	}
}
