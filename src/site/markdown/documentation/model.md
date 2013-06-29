## CLAVIN&#39;s Model

CLAVIN, at its core, is a text-to-geospatial entity toolkit.  To best leverage CLAVIN for your needs, it&apos;s important to understand CLAVIN&apos;s model.

### The Gazetteer Model

The Gazetteer model is CLAVIN's view of the Geospatial world.  It contains the **Places of Interest**, the entities you are most likely using CLAVIN to find, as well as, a semantic model for talking about those places.  In many cases, we have borrowed heavily from the [Geonames](http://geonames.org) model, for which CLAVIN was originally based upon.  We will continue to support a model similar to Geonames (even with different datasources) since we believe it is a rich and fairly complete geospatial model.

> **Note:**  We use the phrase **Place of Interest** (or simply `Place`) instead of **Point of Interest** because we believe it more accurately represents the locations you will find in the text.  For instance, a `Place` can denote a point, a region, or some other complex geometry, where `Point` only speaks to a very distinct position on the globe.

The Gazetteer model is located in package `com.berico.clavin.gazetteer`.

-  `CountryCode` - a enumeration of all known countries.  This is used to normalize gazetteer entries.

-  `FeatureClass` - a top-level category for classifying locations.  Some classes include administrative regions, populated places, hydrographic features (lakes, rivers), and &quot;spot&qout; features (buildings, farms, etc.).

-  `FeatureCode` - a more specific category for classifying a location.  For instance, spot features include things like schools, restaurants, etc.

-  `LatLon` - represents a Latitude and Longitude pair in CLAVIN.  We use `LatLon` as the *Rosetta Stone* for coordinates when we perform reverse resolution.

-  `Place` - a **Place of Interest**.  The `Place` class contains a bunch of useful data including, all of the other model objects listed in this section.

-  `PlaceReference` - an optimization we use to refer to other `Place`s in the gazetteer index.  This is used when talking about *super places*, which are regions in which the target `Place` is contained.

### The Extraction Model

The extraction model deals with unresolved occurrences of resolvable geospatial entities in text.  It can be found in the  `com.berico.clavin.extractor` package.

The root concept of the extraction model is an *occurrence*.  An occurrence is that piece of text in the document that represents the geospatial entity.  All occurrences contain the text that was extracted and the character position in the source document in which the text was found.  You can calculate the *end offset* simply by adding the length of the occurrence to the start position:

```
int endOffset = locationOccurrence.getPosition() + locationOccurrence.getText().length();
```

-  `CoordinateOccurrence<?>` - an occurrence of a coordinate in text (for example:  `34.18N 110.12W`).  The generic typing `<?>` of `CoordinateOccurrence` allows developers to add any type of coordinate system they desire, as long as the coordinate is capable of being converted to a `LatLon`.

-  `LocationOccurrence` - an occurrence of a plain-named location in text (for example: `Boston`).

-  `ExtractionContext` - this class is a &quot;rollup&quot; of the extracted locations and coordinates in the text, as well as, the source text.  We bundle this information together to help insulate the API from changes to the data model in the future (we used to just pass `List<LocationOccurrence>` which caused a lot of maintenance trouble).

### The Resolution Model

Once you have extracted locations and coordinates from text (or maybe you simply started with a list of locations), you probably want to resolve those locations and coordinates to `Place`s.  This is the model you will interact with once the resolution process is finished.

The CLAVIN resolution process is located in the `com.berico.clavin.resolver` package.

-  `ResolvedCoordinate` - a `CoordinateOccurrence` that has been paired with a `Place`.  Typically, you will only receive one `ResolvedCoordinate` for each `CoordinateOccurrence`, but internally, CLAVIN is weighing sets of `ResolvedCoordinate`s for each `CoordinateOccurrence` selecting the best candidate.

-  `ResolvedLocation` - a `LocationOccurrence` paired with a `Place`.  Once again, CLAVIN finds many `ResolvedLocation`s for each `LocationOccurrence` and uses a set of heuristics and optimization techniques to reduce that set to the most likely `Place` the `LocationOccurrence` represents.

-  `Vector` - this is a mathematical vector (direction and magnitude).  We use the `Vector` class to represent the distance and direction a coordinate occurred in relation to the `Place` it has been resolved to.

-  `ResolutionContext` - a &quot;rollup&quot; of the results from resolving both `LocationOccurrence`s and `CoordinateOccurrence`s from the input text.  The `ResolutionContext` also contains the `ExtractionContext` for your convenience.

### CLAVIN Services

CLAVIN now provides a number of service interfaces replacing the old hard-coded workflow.  We will continue to support these interfaces in the future, so it&apos;s a great way to extends a piece of CLAVIN to meet your own needs without having to reengineer the rest of the process.

The three primary interfaces are:

-  `LocationExtractor` - takes in `String` input and returns a set of `LocationOccurrence`s.

-  `CoordinateExtractor` - takes in `String` input and returns a set of `CoordinateOccurrence<?>`s.

-  `LocationResolver` - takes in an `ExtractionContext` and returns a `ResolutionContext`.

Berico&apos;s implementation of the `LocationResolver`, the `DefaultLocationResolver` features five more interfaces:

-  `LocationNameIndex` - takes a `LocationOccurrence` and returns an ordered `List` of `ResolvedLocations`s.  The conventions of ordering are up to the implementation; we realize that `List`s are not `Set`s with `Comparator`s.  The ordering is done purely by order of insert.

-  `CoordinateIndex` - takes a `CoordinateOccurrence` and returns an ordered `List` of `ResolvedCoordinate`s.  The ordering methodology is the same as `LocationNameIndex`.

-  `LocationCandidateSelectionStrategy` - selects the best `ResolvedLocation` for each `LocationOccurrence` from the `List<ResolvedLocation>` returned by the `LocationNameIndex`.  The strategy is also offered the set of `CoordinateOccurrence`s found in the document.  We intend this process to be parallelized, which is why we don't provide the `ResolvedCoordinate`s.

-  `CoordinateCandidateSelectionStrategy` - selects the best `ResolvedCoordinate` for each `CoordinateOccurrence` from the `List<ResolvedCoordinate>` returned by the `CoordinateIndex`.  The strategy is also provided the set of `LocationOccurrence`s and not `ResolvedLocation`s for the same reason as above.

-  `ResolutionResultsReductionStrategy` - an opportunity to review the results of the `CandidateSelectionStrategy`s and filter or aggregate the results as desired.  We plan on performing some result compaction and aggregate analysis in future implementations of this interface. 


### Summary

We hope this provides some illumination to the intent of the CLAVIN model.  Keep in mind that CLAVIN is still maturing, so there will be some updates to the model and interfaces and some future additions.

