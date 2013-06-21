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

### Step 5 - Grab a cold one!

In reality, this shouldn't have even taken 5 minutes!



