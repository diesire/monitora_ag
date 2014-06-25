#!/bin/sh
cd ..
mvn exec:java -Dexec.mainClass="org.hsqldb.Server" -Dexec.args="database.0 file:./pollo/test"
