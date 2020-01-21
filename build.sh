#!/bin/bash

#Copy application.properties

#Edit application.properties

#Skip the test while building
mvn clean install -DskipTests

docker-compose up --build