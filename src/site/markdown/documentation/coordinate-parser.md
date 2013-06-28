## Creating a Coordinate Parser

A major feature of the CLAVIN 0.4.0 release was the introduction of the extraction and reverse resolution of Coordinates.  We interpret `Coordinate` to mean a specific geographic position on the globe represented by a set of numbers (Latitude, Longitude) or a symbol (Geohash).  Since coordinates tend to have a specific schema or set of conventions for how they are used, we generally don't need natural language processing to extract them from text (a simple Regular Expression will do).

This tutorial will explain how to leverage the CLAVIN API to construct and register a new Coordinate parser.  For this example, we will use a [GeoHash](http://geohash.org), since it doesn't require any extra dependencies.  Our GeoHash pattern will be quite contrived.  We will expect a prefix of **geo:** followed by a string of alphanumeric characters representing the hash.  Our canonical examples is **geo:u4pruydqqvjs**, which is the GeoHash mentioned on [Wikipedia - GeoHash](http://en.wikipedia.org/wiki/Geohash).

To create a coordinate parser, you will need to create three new classes.  These classes are rather trivial, so don't worry:

1.  Coordinate Model  - this will symbolically represent an instance of the coordinate specific to it's coordinate system.
2.  Coordinate Occurrence - this is an instance of `CoordinateOccurrence` specific to your custom coordinate model.
3.  Coordinate Parsing Strategy - this is a mechanism that will locate the occurrence of the coordinate in text.

For this example, we will create the following classes:

1.  `GeoHash` - representing a GeoHash coordinate.
2.  `GeoHashOccurrence` - an occurrence of a `GeoHash` in text.
3.  `GeoHashParsingStrategy` - a strategy for locating and parsing the `GeoHash` from text and returning a `GeoHashOccurrence`.

> All of these classes can be found in the `com.berico.clavin.examples` package.

### Step 1 - The `GeoHash` class.

The `GeoHash` class is very simple.  It will only have one property, `hashValue`, which will represent the String-based Geohash.  The class will also contain a method to return the `LatLon` value of the Geohash. `LatLon` is the **Rosetta Stone** of coordinates used by CLAVIN; all coordinate occurrences must be able to return a `LatLon` value which is used by the indexer to perform the reverse lookup.

```
import com.berico.clavin.gazetteer.LatLon;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.io.GeohashUtils;
import com.spatial4j.core.shape.Point;

public class GeoHash {

  private String hashValue;
  private LatLon latlonValue;
  
  /**
   * For serialization purposes.
   */
  public GeoHash(){}
  
  /**
   * Initialize with the GeoHash value.
   * @param hashValue GeoHash value.
   */
  public GeoHash(String hashValue){
    
    this.hashValue = hashValue;
  }

  /**
   * Get the GeoHash Value.
   * @return GeoHash Value.
   */
  public String getHashValue() {
    return hashValue;
  }
  
  /**
   * Get the LatLon Value of the GeoHash.
   * @return LatLon value.
   */
  public LatLon getLatLonValue(){
    
    if (latlonValue == null){
      
      // Spatial4J's Geohashing functionality.
      Point p = GeohashUtils.decode(this.hashValue, SpatialContext.GEO);
    
      latlonValue = new LatLon(p.getY(), p.getX());
    }
    
    return latlonValue;
  }
  
}
```

### Step 2 - The `GeoHashOccurrence` class.

The `GeoHashOccurrence` class represent the finding of a `GeoHash` in a document.  It contains a character position in text, the extracted string representing the coordinate, and the actual coordinate value. The `GeoHashOccurrence` class must implement the `CoordinateOccurrence<GeoHash>`
interface in order to be resolved by the `LocationResolver`.  We've extended the abstract `BaseCoordinateOccurrence` class, which reduces some of the setter/getter boilerplate and directly fulfills some of the `CoordinateOccurrence<?>` requirement's.

```
import com.berico.clavin.extractor.coords.BaseCoordinateOccurrence;
import com.berico.clavin.gazetteer.LatLon;

public class GeoHashOccurrence extends BaseCoordinateOccurrence<GeoHash>  {
  
  /**
   * For serialization purposes.
   */
  public GeoHashOccurrence() { super(); }

  /**
   * Initialize with the GeoHash value.
   * @param position Position in document.
   * @param text Extracted GeoHash text.
   * @param value The GeoHash.
   */
  public GeoHashOccurrence(long position, String text, GeoHash value) {
    super(position, text, value);
  }

  /**
   * Get the coordinate system, in this case, "geohash".
   * @return "geohash"
   */
  @Override
  public String getCoordinateSystem() {
    
    return "geohash";
  }

  /**
   * Convert the coordinate to it's LatLon representation.
   * @return LatLon value of the coordinate.
   */
  @Override
  public LatLon convertToLatLon() {
    
    return this.value.getLatLonValue();
  }

}
```

### Step 3 - The `GeoHashParsingStrategy` class.

The `GeoHashParsingStrategy` is a simple class that provides a REGEX pattern to a container class (`RegexCoordinateExtractor`), as well as, a means of extracting the coordinate from matching Strings.  One additional feature we&apos;ve added is support for [Named Capture Groups](http://www.regular-expressions.info/named.html) in Regular Expressions, even for versions of Java as old as 1.5 (this feature did not come into Java until version 7).  We take advantage of the Named Capture Groups liberally in our implementations of the Latitude and Longitude extractors.

Another this to add is that you are given the start position and matching string.  It&apos; responsibility to return a `CoordinateOccurrence`, of which, these are two required pieces.  The reason why we don&apos;t do this for you is to support REGEX's that may need to match larger pieces of content that include more than just the coordinate text.

```
import java.util.Map;
import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.extractor.coords.RegexCoordinateParsingStrategy;
import com.google.code.regexp.Pattern;

public class GeoHashParsingStrategy implements RegexCoordinateParsingStrategy<GeoHash> {

  public static final String GEOHASH_PATTERN = "(geo[:](?<hash>\\w*))";
  
  /**
   * Return a compiled REGEX with the GeoHash pattern.
   * @return Compiled REGEX Pattern
   */
  @Override
  public Pattern getPattern() {
    
    return Pattern.compile(GEOHASH_PATTERN);
  }

  /**
   * Parse the GeoHash from the returned REGEX named groups, returning
   * a GeoHashOccurrence.
   * @param matchedString String matching the REGEX statement in the document.
   * @param namedGroups REGEX named capture groups.
   * @param startPosition The position the occurrence occurred in the document.
   * @return A GeoHashOccurrence.
   */
  @Override
  public CoordinateOccurrence<GeoHash> parse(
      String matchedString, 
      Map<String, String> namedGroups, 
      int startPosition) {
    
    String geohashString = namedGroups.get("hash");
    
    GeoHash geohash = new GeoHash(geohashString);
    
    return new GeoHashOccurrence(startPosition, matchedString, geohash);
  }

}
```

### Step 4 - Register `GeoHashParsingStrategy` with the `GeoParserFactory`.

Probably the easiest way to add support for your new coordinate system is to register it with the `GeoParserFactory`.  The `GeoParserFactory` provides the default configuration for a `GeoParser`, which can be quite complicated to instantiate with all of its dependencies.

```
GeoParserFactory
      .DefaultCoordinateParsingStrategies
        .add(new GeoHashParsingStrategy());
```

### Step 5 - That&apos;s it, Perform an extraction.

And here's a final example of the process working (`com.berico.clavin.examples.GeoHashDemo`):

```
import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.extractor.CoordinateOccurrence;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;

public class GeoHashDemo {
  
  public static void main(String[] args) throws Exception {
    
    // Register the new Parsing Strategy.
    GeoParserFactory
      .DefaultCoordinateParsingStrategies
        .add(new GeoHashParsingStrategy());
    
    // Get a parser instance.
    GeoParser parser = GeoParserFactory.getDefault("IndexDirectory/");
    
    // Parse the document.
    ResolutionContext results = 
      parser.parse("Hey mom, I went to geo:u4pruydqqvjs today!");
    
    // Get the CoordinateOccurrence from the Extraction Context
    CoordinateOccurrence<?> ecoord = 
        results.getExtractionContext().getCoordinates().get(0);
    
    System.out.println(ecoord);
    
    // Get the ResolvedCoordinate from the results
    ResolvedCoordinate rcoord = results.getCoordinates().get(0);
    
    System.out.println(rcoord);
  }
}
```

And you should see the following output to the console:

<pre>
17:24:57.836 [main] INFO  com.berico.clavin.GeoParser - Input Size: 42
17:24:57.852 [main] INFO  com.berico.clavin.GeoParser - Extracted Location Count: 0
17:24:57.858 [main] INFO  com.berico.clavin.GeoParser - Extracted Coordinates Count: 1
17:24:57.859 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Beginning resolution step.
17:24:57.859 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 0 location candidate lists.
17:24:58.400 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 1 coordinate candidate lists.
New Best: 24.033824275546056, Loc: Resolved: "geo:u4pruydqqvjs" as 0.07398139381297736km NE of Råbjerg Mile, [DK]
17:24:58.402 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 1 coordinates.
17:24:58.403 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 0 locations.
17:24:58.403 [main] INFO  com.berico.clavin.GeoParser - Resolved 0 locations and 1 coordinates.
com.berico.clavin.examples.GeoHashOccurrence: 
  Text: geo:u4pruydqqvjs
  Position: 19
  Value: com.berico.clavin.examples.GeoHash@79c0f654

Resolved: "geo:u4pruydqqvjs" as 0.07398139381297736km NE of Råbjerg Mile, [DK]
</pre>

### Step 5 - Go have another cold one!

Pretty easy, huh?


