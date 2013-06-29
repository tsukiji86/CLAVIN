## Building a Gazetteer Index From Geonames

Clavin features a `GeonamesIndexBuilder` implementation that will create an index based on the Geonmaes.org dataset.  There is a helper script in the `scripts` directory called `build-geonames-index.sh` that will assist you in this process.

From the base of the CLAVIN project directory:

```
sh scripts/build-geonames-index.sh
```

This process will take a while.  It will first download the most recent Geonames.org Gazetteer, extract it (it's a zip file), and then begin the laborious process of parsing each line of the text file, converting it into a `Place` and then passing it to it's parent class `IndexBuilder` which will take care of the Lucene-specific indexing details.

The `GeonamesIndexBuilder`, however, is a great example in showing what's involved in creating your own custom index.  We've gone out of our way to simplify the index creation process by providing you a set of tools to make this process easy.

If you need detailed instructions on how to extend the `IndexBuilder` class to create a custom index, refer to the tutorial [Building a Gazetteer Index From a Custom Source](custom-indexing.html).