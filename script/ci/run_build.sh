#!/usr/bin/env sh
curl http://download.geonames.org/export/dump/allCountries.zip -o allCountries.zip
unzip allCountries.zip
head -n 100 allCountries.txt > small_index.txt
tail -n 100 allCountries.txt >> small_index.txt
mv -f small_index.txt allCountries.txt
mvn compile
MAVEN_OPTS="-Xmx4g" mvn exec:java -Dexec.mainClass="com.bericotech.clavin.index.IndexDirectoryBuilder"
mvn package

