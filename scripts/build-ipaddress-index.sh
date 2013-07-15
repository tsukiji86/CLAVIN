#!/usr/bin/env sh

# This should be wherever you want the index located.
INDEXDIR="./GeoIP"

if [ -d "$INDEXDIR" ]; then
  echo "Deleting existing index."
  rm -R "$INDEXDIR"
fi

mkdir -p $INDEXDIR
curl http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz -o $INDEXDIR/GeoLite2-City.mmdb.gz
gunzip $INDEXDIR/GeoLite2-City.mmdb.gz
