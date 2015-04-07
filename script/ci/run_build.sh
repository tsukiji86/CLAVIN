#!/usr/bin/env sh
mvn compile
mvn package -DskipTests=true -Dgpg.skip=true

