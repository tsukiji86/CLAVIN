#!/usr/bin/env sh
curl http://download.geonames.org/export/dump/allCountries.zip -o allCountries.zip
unzip allCountries.zip
mvn compile
mvn exec:java -Dexec.mainClass="com.berico.clavin.resolver.impl.lucene.GeonamesIndexBuilder" -Dexec.args="./IndexDirectory allCountries.txt"
mvn package