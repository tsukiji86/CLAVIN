#!/usr/bin/env sh

TAGLINE=$1
SSN=$2

# This should be wherever you want the index located.
INDEXDIR="./CustomExampleIndexDirectory"

if [ -d "$INDEXDIR" ]; then
  echo "Deleting existing index."
  rm -R "$INDEXDIR"
fi

mvn clean compile

export MAVEN_OPTS="-Xmx2g"

mvn exec:java  -Dexec.mainClass="com.berico.clavin.examples.CustomIndexBuilder" -Dexec.args="$INDEXDIR $TAGLINE $SSN"
