#!/bin/bash

DIR=./build
if [ ! -d "$DIR" ]; then
        mkdir build
fi

find src -name "*.java" > sources.txt
javac -Xlint:deprecation -d build @sources.txt
