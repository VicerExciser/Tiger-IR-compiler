#!/bin/bash

if [ -d "build" ]; then
	# echo "removing build"
	rm -rf build/
fi
mkdir build
javac -d build -cp src src/main/java/mips/MIPSInterpreter.java
if [ $# -lt 1 ]; then
	FILE="../test.s"
else
	FILE="$1"
fi
java -cp build main.java.mips.MIPSInterpreter $FILE

