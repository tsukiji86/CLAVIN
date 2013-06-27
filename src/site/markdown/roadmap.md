# Roadmap

There is a laundry list of features we plan on adding to CLAVIN core.  This list, while not exhaustive, should include most of them.

### Analytic Improvements
-  Filter out demonyms.
-  Bias resolution towards popular locations.
-  Improve handling of abbreviations (like States).
-  Proximity between locations as a weight for resolution of plain-named locations (coordinates already do this).
-  Contextual resolution support for frequently co-ocurring entities (via Lucene Term Vectors).

### API Improvements
-  OSGi Support - turning primary components like the extractors, resolvers and indexes into OSGi services.
-  Resolve PlaceReferences during index creation (`postProcess()` step to `IndexBuilder`).
-  Store Polygons (more complex geometries) with Place data in the index.
-  Use String-based ID's for Places.
-  Store source Gazetteer on Place with a Gazetteer ID field.

### Feature Enhancements
-  Output as a ranked list of locations.
-  Resolve IP Addresses.
-  Resolve telephone numbers by Area Code.
-  OpenStreetMap Gazetteer IndexBuilder.
-  Better workflow for resolving structured data (like Excel and CSV).
-  Resolve temporal references in addition to geospatial locations.
