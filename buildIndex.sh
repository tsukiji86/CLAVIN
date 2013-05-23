#!/bin/bash

GEONAMES="allCountries.txt"
INDEXDIR="./IndexDirectory"

if [ -d "$INDEXDIR" ]; then
  echo "Deleting existing index."
  rm -R "$INDEXDIR"
fi

if [ ! -f "$GEONAMES" ]; then
  echo "Downloading GeoNames Gazetteer"
  curl http://download.geonames.org/export/dump/allCountries.zip -o allCountries.zip
  unzip allCountries.zip
  rm allCountries.zip
fi

mvn compile
mvn exec:java -Dexec.mainClass="com.berico.clavin.index.IndexDirectoryBuilder" -Dexec.args="-Xmx2g"