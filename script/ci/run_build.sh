#!/usr/bin/env sh
curl http://download.geonames.org/export/dump/allCountries.zip -o allCountries.zip
unzip allCountries.zip
head -n 100 allCountries.txt > small_index.txt
grep Virginia allCountries.txt >> small_index.txt
grep Vermont allCountries.txt >> small_index.txt
grep Illinois allCountries.txt >> small_index.txt
grep Missouri allCountries.txt >> small_index.txt
grep Oregon allCountries.txt >> small_index.txt
grep Springfield allCountries.txt >> small_index.txt
grep Massachusetts allCountries.txt >> small_index.txt
grep Bostonn allCountries.txt >> small_index.txt
grep lond allCountries.txt >> small_index.txt
grep "Straßenhaus" allCountries.txt >> small_index.txt
grep "Hong Kong Special Administrative Region" allCountries.txt >> small_index.txt
grep "Clipperton Island" allCountries.txt >> small_index.txt
grep "United States" allCountries.txt >> small_index.txt
grep "Soviet Union" allCountries.txt >> small_index.txt
tail -n 100 allCountries.txt >> small_index.txt
mv -f small_index.txt allCountries.txt
mvn compile
MAVEN_OPTS="-Xmx4g" mvn exec:java -Dexec.mainClass="com.bericotech.clavin.index.IndexDirectoryBuilder"
mvn package

