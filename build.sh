#!/bin/sh
~/maven/bin/mvn package
java -jar target/photo-friend-1.0-SNAPSHOT.jar
