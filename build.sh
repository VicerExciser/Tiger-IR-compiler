#!/bin/bash

DIR=./build
if [ ! -d "$DIR" ]; then
        mkdir build
fi

find src -name "*.java" > sources.txt
javac -d build @sources.txt

