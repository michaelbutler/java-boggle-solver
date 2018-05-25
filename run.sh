#!/bin/bash

# Usage: ./run.sh 

java -classpath "classes:./vendor/commons-cli/target/commons-cli-1.2.jar" \
    net.butlerpc.boggle.Main \
    $1 $2 $3 $4 $5 $6
