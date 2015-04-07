#!/usr/bin/env sh
curl http://download.geonames.org/export/dump/allCountries.zip -o allCountries.zip
unzip allCountries.zip
mvn compile
mvn package

