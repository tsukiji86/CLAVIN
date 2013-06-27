#!/usr/bin/env sh

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

mvn clean compile

export MAVEN_OPTS="-Xmx2g"

mvn exec:java  -Dexec.mainClass="com.berico.clavin.resolver.impl.lucene.GeonamesIndexBuilder" -Dexec.args="$INDEXDIR $GEONAMES"

# Remove the allCountries.txt if the indexing 
#rc=$?
#if [[ $rc == 0 ]] ; then
#  rm $GEONAMES
#fi