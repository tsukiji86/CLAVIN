# 5-Minute Tutorial 

This is the simplest example of using CLAVIN.  In this example, we will obtain the default instance of the geoparser and resolve locations found in a plain text file (the file actually located in the CLAVIN project directory).

### Before you start.

1.  If you have not built your Gazatteer index, please follow the directions here: [Building a Gazetteer Index from Geonames](../documentation/geonames-indexing.html).

2.  Please ensure you have CLAVIN on your classpath, whether via dependency management ([Maven Dependency Info](installation.html)) or by directly referencing the JAR.

3.  In this demonstration, we're using the following imports:

```
import java.io.File;
import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.util.TextUtils;
```

### Step 1 -  Obtain a `GeoParser` instance.

The `GeoParser` class represents a the most basic CLAVIN geoparsing workflow.  To simplify the process of instantiating a `GeoParser` class, we have provided a `GeoParserFactory` that will take care of wiring up the correct classes based on the method and parameters you call.

```
GeoParser parser = GeoParserFactory.getDefault("./IndexDirectory");
```

In this instance, we are passing the location of an embedded Lucene Index Directory, that was built when you ran the index creation script.


### Step 2 - Obtain the source text.

Now you need to obtain the text in which you want locations extracted and resolved.  In this example, we will read a file from the file system as a string.

```
String input = TextUtils.fileToString( 
    new File( "src/test/resources/sample-docs/Somalia-doc.txt" ) );
```

###  Step 3 - Resolve the locations from the text.

Now you will perform the resolution.

```
ResolutionContext results = parser.resolveLocations( input );
```

### Step 4 - Play with the results.

In this example, we'll iterate over the plain-named locations found in text, printing their name, latitude, longitude and character position to the console.

```
for (ResolvedLocation location : results.getLocations(){

   String msg = String.format(
      "%s [%s, %s] was mentioned at character position %s", 
      location.getPlace().getName(), 
      location.getPlace().getCenter().getLatitude(),
      location.getPlace().getCenter().getLongitude(),
      location.getLocation().getPosition());

   System.out.println( msg );
}
```

### Step 5 - Run your app.

If you are using Maven, this is really simple:

```
MAVEN_OPTS="-Xmx2048M" mvn exec:java -Dexec.mainClass="your.namespace.YourClass"
```

This demonstration is identical to the `WorkflowDemo` example:

```
MAVEN_OPTS="-Xmx2048M" mvn exec:java -Dexec.mainClass="com.berico.clavin.examples.WorkflowDemo"
```

You're output should look something like this:

```
14:05:36.625 [...WorkflowDemo.main()] INFO  com.berico.clavin.GeoParser - Input Size: 27837
14:05:37.360 [...WorkflowDemo.main()] INFO  com.berico.clavin.GeoParser - Extracted Location Count: 42
14:05:37.381 [...WorkflowDemo.main()] INFO  com.berico.clavin.GeoParser - Extracted Coordinates Count: 0
14:05:37.382 [...WorkflowDemo.main()] DEBUG c.b.c.r.impl.DefaultLocationResolver - Beginning resolution step.
14:05:37.611 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for european union.
14:05:37.737 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for hamar\-weyne.
14:05:37.744 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for birmingham uk..
14:05:37.770 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for southern somalia.
14:05:37.872 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for kismayo.
14:05:37.883 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for las\-anod.
14:05:37.961 [...WorkflowDemo.main()] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for roadmap.
14:05:37.962 [...WorkflowDemo.main()] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 35 location candidate lists.
14:05:37.962 [...WorkflowDemo.main()] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 0 coordinate candidate lists.
14:05:37.962 [...WorkflowDemo.main()] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 0 coordinates.
14:05:38.055 [...WorkflowDemo.main()] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 35 locations.
14:05:38.056 [...WorkflowDemo.main()] INFO  com.berico.clavin.GeoParser - Resolved 35 locations and 0 coordinates.
Greater London [51.5, -0.16667] was mentioned at character position 2306
Africa [32.99568, -90.77121] was mentioned at character position 2586
Muna [20.48628, -89.71399] was mentioned at character position 2957
Matabaan [5.20401, 45.53353] was mentioned at character position 4161
City of Birmingham [42.54466, -83.21667] was mentioned at character position 4841
Hirān [22.9976, 89.95914] was mentioned at character position 5208
Greater London [51.5, -0.16667] was mentioned at character position 6129
Africa [1.07818, -79.2871] was mentioned at character position 6724
Presbyterian Church of East Africa [-1.2564, 36.86494] was mentioned at character position 7589
Africa [1.07818, -79.2871] was mentioned at character position 7620
Greater London [51.5, -0.16667] was mentioned at character position 7643
Somali [-22.33333, 19.9] was mentioned at character position 11059
Mashonaland East Province [-17.5, 32.0] was mentioned at character position 11422
Kenya [-22.55, 144.25] was mentioned at character position 12822
Ethiopia Shoal [9.75, 80.38333] was mentioned at character position 12832
Bi’r al Banādir [32.54556, 21.58056] was mentioned at character position 14545
Muna [20.48628, -89.71399] was mentioned at character position 15403
Parliament [53.34417, -6.26774] was mentioned at character position 16710
Puntland [9.0, 49.0] was mentioned at character position 17873
Sool [2.80412, 44.04773] was mentioned at character position 18102
Somalia [11.63456, 43.34036] was mentioned at character position 18126
Somalia [11.63456, 43.34036] was mentioned at character position 18224
Sool [2.80412, 44.04773] was mentioned at character position 18307
Kenya [-22.55, 144.25] was mentioned at character position 18718
Kenya [-22.55, 144.25] was mentioned at character position 19151
Gedo [-8.7268, 122.2866] was mentioned at character position 19404
Kenya [-22.55, 144.25] was mentioned at character position 19428
Russia [40.23449, -84.40939] was mentioned at character position 19476
Muna [-9.9004, 124.1817] was mentioned at character position 20487
City of New Britain [41.67581, -72.78623] was mentioned at character position 22502
Africa [32.99568, -90.77121] was mentioned at character position 22753
City of New Britain [41.67581, -72.78623] was mentioned at character position 23251
Yemen [47.7083, -110.72577] was mentioned at character position 23352
Embassy of the Federal Republic of Nigeria [38.90539, -77.04914] was mentioned at character position 23362
‘Irāq al Ḩamrā’ [32.21667, 35.43333] was mentioned at character position 23404
```

### Step 6 - Grab a cold one!

In reality, this shouldn't have even taken 5 minutes!



