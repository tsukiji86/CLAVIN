#!/usr/bin/env sh
curl http://download.geonames.org/export/dump/allCountries.zip -o allCountries.zip
mvn compile
chmod +x ./bin/clavin
./bin/clavin index 
mvn package

