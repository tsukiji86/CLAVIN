# Overview

### Description

<img src="images/clavin_logo.png" style="float:right; height: 14em; margin: 25px" />

CLAVIN (Cartographic Location And Vicinity INdexer) is an open source software package for document geotagging and geoparsing that employs context-based geographic entity resolution.

It extracts location names from unstructured text and resolves them against a gazetteer to produce data-rich geographic entities.

CLAVIN does not simply "look up" location names – it uses intelligent heuristics to identify exactly which "Springfield" (for example) was intended by the author, based on the context of the document. CLAVIN also employs fuzzy search to handle incorrectly-spelled location names, and it recognizes alternative names (e.g., "Ivory Coast" and "Côte d'Ivoire") as referring to the same geographic entity.

By enriching text documents with structured geo data, CLAVIN enables hierarchical geospatial search and advanced geospatial analytics on unstructured data.

### History

CLAVIN started to address an unsatisfied need for georectifying plain-named locations for a customer of Berico Technologies.  At the time, we were building the first incarnation of Rivium4, a full-text search application that incorporated geospatial entities as facets.  Originally, another organization was to provide the geospatial locations in our source data, but due to time constraints, that organization was unable to deliver the feature.  Since geospatial search was an absolute requirement for the application, Charlie Greenbacker and other Berico engineers embarked on building their own geoparser.   Within a couple of months, the team had successfully built the first iteration of what came to be known as CLAVIN.

CLAVIN was deployed into production at the customer site in the summer of 2012, as a key component of the customer's analytic pipeline built on top of Apache Hadoop.  CLAVIN has since been deployed into three more of our (Berico's) customer environments.  We also know of it's use by other companies and academic institutions, though to what capacity we cannot say.
 
**So what's up with the name?**  

CLAVIN is named after a Cliff Clavin, the lovable know-it-all postal worker on the 1980's sitcom *Cheers*.  In fact, CLAVIN is a **backronym**, "a phrase specially constructed so that an acronym fits an existing word" [Wikipedia ~ Backronym](http://en.wikipedia.org/wiki/Backronym).
 
### License

Copyright (C) 2012-2013 Berico Technologies

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.