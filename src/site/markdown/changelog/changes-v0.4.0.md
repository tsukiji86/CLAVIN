# Changelog v0.4.0

Version 0.4.0 represents a major departure from the old CLAVIN API.  The primary intent for version 0.4.0 was to support coordinate (e.g. latitude/longitude) extraction from plain text and reverse resolution (finding the most likely named location).

Unfortunately, the original CLAVIN API made it very difficult to efficiently add this feature, as well as, a number of planned features (like supporting other indexing technologies).  Instead of putting a *bandaid* on the version 0.3.3, we decided to add the essential features that will be necessary to maintaining CLAVIN in the future.

This list details most of the major and minor changes made to the CLAVIN API:

### Additions
-  Generic interfaces for coordinate extraction.  Developers should now be able to write their own extractors for various coordinate (or coordinate-like) systems.
-  Abstract classes to support regular expression based coordinate extraction, making it easy to write new coordinate parsers.  We're already thinking about using this technique to support GeoIP and Geotag resolution.
-  Support for the extraction of latitude and longitude in Degree-Minutes-Seconds (DMS) form.
-  Support for the extraction of latitude and longitude in Decimal Degrees (DD) form.
-  `LatLonPair` class to represent a canonical representation of Lat/Lon in CLAVIN.
-  `Vector` class to represent distance and direction of a coordinate from a known location.
-  `ResolvedCoordinate` class to represent the most probable named location for a coordinate with an associated vector from that location.
-  `PlaceReference` class to better model associate (linked) places like administrative hierarchies (states and countries) or super locales (continents).
-  The **Maven Site** plugin was integrated to give users a more rubust set of documentation (obviously, since this content was created via Maven Site!).

### Changes
-  CLAVIN's heuristic tests are now *integration tests* and will not be run by default when the CLAVIN package is created.  A number of other non-unit tests were added, all of which are considered integration tests (with the suffix `IT.java`, not `Test.java`).  To run integration tests, simply add the flag `-DskipTests=false` to the end of your Maven command.
-  Example classes have been moved to the `com.berico.clavin.examples` namespace for clarity purposes.
-  Removed the direct dependency on the Geonames model via the `Geonames` class.  CLAVIN now uses a "Geonames" inspired model, retaining some of the Ontology-related fixtures (like feature classes), but now refers to this entity as a `Place`.
-  Places are now serialized and deserialized from the index using the default resolver: `com.berico.clavin.utils.Serializer.Default`.  The default implementation is JSON (`JsonSerializer`), provided by `Gson`.
-  Removed the direct dependency on Lucene by introducing a `LocationResolver` interface.
-  Introduced the concept of an `ExtractionContext` and a `ResolutionContext` to insulate the `LocationExtractor` and `LocationResolver` interfaces from future changes.
-  Added support for radius-based geospatial look ups of locations to support reverse resolution of coordinates.
-  Abstracted the tedious number of Lucene components (particularly the spatial components) into a `LuceneComponents` class.
-  Changed the Lucene indexing strategy to use multi-valued fields instead of storing each alternateName (and ASCII name) as a separate entry in the index.  This produced an enormous performance boost compared to the old method.
-  Reusing Lucene fields during indexing, which periodicity of garbage collection during index building.
-  Introduced a weight based resolution strategy for resolving coordinates and plain-text locations.  While a weighting strategy for plain-text locations is likely to be less accurate, it will provide a major performance boost by allowing the resolution process to parallelized. 
-  `IndexDirectoryBuilder` was replaced by an abstract `IndexBuilder`, a class to assist in the creation of indices, and the `GeonamesIndexBuilder` which functionally does the same thing that `IndexDirectoryBuilder` used to do. 
