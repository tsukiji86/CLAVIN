## Resolving Locations in other Languages

It is possible for CLAVIN to be used for geoparsing documents in other languages without much modification.  The only component that really needs to be modified is the LocationExtractor, since it is that component&apos;s responsibility for finding unresolved locations in text.

The Apache OpenNLP NameFinder supports two languages other than English: Dutch and Spanish.  In this example, we will use both to demonstrate the process.

### Step 1 - Download the OpenNLP Language models.

Luckily for you, this has already been done.  Those models are in the CLAVIN project (`src/main/java/resources`).

If you want to download them yourself, you can find them on the [OpenNLP SourceForge Site](http://opennlp.sourceforge.net/models-1.5/).

### Step 2 - Configure OpenNLP to use the new language models.

Create an instance of the `ApacheExtractor` and point it to the correct models:


##### For Dutch use the following configuration:

```
ApacheExtractor locationExtractor = 
  new ApacheExtractor(
    "src/main/resources/nl-ner-location.bin", 
    "src/main/resources/nl-token.bin", 
    "src/main/resources/nl-sent.bin");
```

##### For Spanish use the following configuration:

```
ApacheExtractor locationExtractor = 
  new ApacheExtractor(
    "src/main/resources/es-ner-location.bin", 
    "src/main/resources/en-token.bin", 
    "src/main/resources/en-sent.bin");
```

> Note:  We are using the English tokenizers for the Spanish corpus since the languages are similar enough and the fact that OpenNLP doesn&apos;t actually have the models for the Spanish language.


### Step 3 - Override the `DefaultLocationExtractor` of `GeoParserFactory`

Let&apos;s make change the default location extractor:

```
GeoParserFactory.DefaultLocationExtractor = locationExtractor;
```

### Step 4 - Perform resolution as you normally would

Here is an example of the regular workflow (in this example we are parsing a Spanish document):

```
// Get an instance of the GeoParser.
GeoParser geoParser = GeoParserFactory.getDefault("IndexDirectory/");

// Read the test article to a String.
String spanishText = TextUtils.fileToString(
    new File("src/test/resources/sample-docs/SpanishNewsArticle.txt"));

// Parse the article.
ResolutionContext results = geoParser.parse(spanishText);

// Play with the results.
for (LocationOccurrence location : results.getExtractionContext().getLocations()){
  
  System.out.println(location);
}

System.out.println("----------------------------------------------------------");

for (ResolvedLocation location : results.getLocations()){
  
  System.out.println(location);
}
```

There is also a Dutch news article available:

```
String dutchText = TextUtils.fileToString(
  new File("src/test/resources/sample-docs/DutchNewsArticle.txt"));
```

###  Dutch Results

<pre>
21:34:40.636 [main] INFO  com.berico.clavin.GeoParser - Input Size: 2852
21:34:40.829 [main] INFO  com.berico.clavin.GeoParser - Extracted Location Count: 7
21:34:40.840 [main] INFO  com.berico.clavin.GeoParser - Extracted Coordinates Count: 0
21:34:40.841 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Beginning resolution step.
21:34:41.597 [main] INFO  c.b.c.r.i.l.LuceneLocationNameIndex - Found no results for duitsland.
21:34:41.597 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 6 location candidate lists.
21:34:41.597 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 0 coordinate candidate lists.
21:34:41.598 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 0 coordinates.
21:34:41.602 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 6 locations.
21:34:41.603 [main] INFO  com.berico.clavin.GeoParser - Resolved 6 locations and 0 coordinates.
LocationOccurrence: [Brussel] at position 458.
LocationOccurrence: [Nederland] at position 468.
LocationOccurrence: [Nederland] at position 671.
LocationOccurrence: [Nederland] at position 810.
LocationOccurrence: [Nederland] at position 848.
LocationOccurrence: [Nederland] at position 890.
LocationOccurrence: [Duitsland] at position 1596.
----------------------------------------------------------
Resolved "Brussel" as"Brussel" 
  { Place [name: Brussel, country: ZA, center: -29.3029, 21.24889]}, 
  position: 458, confidence: 0.0, fuzzy: false
Resolved "Nederland" as"Nederland" 
  { Place [name: Nederland, country: US, center: 29.97438, -93.9924]}, 
  position: 468, confidence: 0.0, fuzzy: false
Resolved "Nederland" as"Nederland" 
  { Place [name: Nederland, country: US, center: 29.97438, -93.9924]}, 
  position: 671, confidence: 0.0, fuzzy: false
Resolved "Nederland" as"Nederland" 
  { Place [name: Nederland, country: US, center: 29.97438, -93.9924]}, 
  position: 810, confidence: 0.0, fuzzy: false
Resolved "Nederland" as"Nederland" 
  { Place [name: Nederland, country: US, center: 29.97438, -93.9924]}, 
  position: 848, confidence: 0.0, fuzzy: false
Resolved "Nederland" as"Nederland" 
  { Place [name: Nederland, country: US, center: 29.97438, -93.9924]}, 
  position: 890, confidence: 0.0, fuzzy: false
</pre>

### Spanish Results

<pre>
21:36:13.577 [main] INFO  com.berico.clavin.GeoParser - Input Size: 3757
21:36:13.808 [main] INFO  com.berico.clavin.GeoParser - Extracted Location Count: 4
21:36:13.824 [main] DEBUG c.b.c.e.c.BaseDdPatternParsingStrategy - From '28.144.000 e', parsed Lat/Lon: 28.14, 4.0.
21:36:13.866 [main] INFO  com.berico.clavin.GeoParser - Extracted Coordinates Count: 1
21:36:13.867 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Beginning resolution step.
21:36:14.434 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 4 location candidate lists.
21:36:14.572 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 1 coordinate candidate lists.
New Best: 0.10391060131503002, Loc: Resolved: "28.144.000 e" as 19.247314274859388km W of Tisnaïet el Khahna, [DZ]
New Best: 0.1837341597761688, Loc: Resolved: "28.144.000 e" as 10.885292111366052km ESE of ’Aïn el Morra, [DZ]
New Best: 0.19975483724644835, Loc: Resolved: "28.144.000 e" as 10.012273182313436km SSW of Djebel el Abiod, [DZ]
21:36:14.573 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 1 coordinates.
21:36:14.579 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 4 locations.
21:36:14.579 [main] INFO  com.berico.clavin.GeoParser - Resolved 4 locations and 1 coordinates.
LocationOccurrence: [Uruguay] at position 1562.
LocationOccurrence: [Adade] at position 2313.
LocationOccurrence: [Madrid] at position 2550.
LocationOccurrence: [Suiza] at position 2706.
----------------------------------------------------------
Resolved "Uruguay" as"Uruguay" 
  { Place [name: Uruguay, country: CU, center: 21.18333, -77.8]}, 
  position: 1562, confidence: 0.0, fuzzy: false
Resolved "Adade" as"Adade" 
  { Place [name: Adad, country: DZ, center: 23.24072, 5.80027]}, 
  position: 2313, confidence: 0.0, fuzzy: false
Resolved "Madrid" as"Madrid Centro" 
  { Place [name: City Center, country: ES, center: 40.41831, -3.70275]}, 
  position: 2550, confidence: 7.0, fuzzy: false
Resolved "Suiza" as"Suiza" 
  { Place [name: La Suiza, country: CR, center: 9.85238, -83.61331]}, 
  position: 2706, confidence: 0.0, fuzzy: false
</pre>

### Summary

We hope you noticed that the results were kind of awful.  Please don't use the OpenNLP language models without additional training since a Google Translate of the source documents will demonstrate how many locations were missed by the extractor.  CLAVIN didn't do terribly well at resolving some of the Dutch names (i.e. Nederlands), which is an indication that the underlying Gazetteer (GeoNames) needs some additional entries.