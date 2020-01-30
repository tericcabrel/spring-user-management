#!/bin/bash

cd src/main/resources

mv application.properties application.old.properties

cp application-example.properties application.properties

nano application.properties

cd ../../..

# Skip the test while building
mvn clean install -DskipTests

mv src/main/resources/application.old.properties src/main/resources/application.properties

docker-compose up --build