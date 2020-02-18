#!/bin/bash

##################################################################
### [1.]  Runs '.in' test file using the provided '.ir' file
###
### [2.]  Optimizes the provided '.ir' file using IROptimizer.java,
###       outputs the optimized IR code to the file 'optimized.ir'
###
### [3.]  Runs '.in' test file using our optimized '.ir' file
##################################################################

clean_build() {
	if [ -d "build" ]; then
		rm -rf build/
		rm -f **/*.class
	fi
	mkdir build 
	find src -name "*.java" > sources.txt
	javac -d ./build @sources.txt
}

if [[ $# -lt 1 ]]; then
	echo -e "Missing test file arguments - please run with:\n\t./runtest.sh <path/to/test.ir> <path/to/test.in>"
elif [[ $# -lt 2 ]]; then 
	echo -e "Missing test input file argument - please run with:\n\t./runtest.sh <path/to/test.ir> <path/to/test.in>"
else
	clean_build
	echo -e "\nBefore optimization:"
	java -cp ./build IRInterpreter $1 < $2
	java -cp ./build IROptimizer $1 optimized.ir > /dev/null
	echo -e "\nAfter optimization:"
	java -cp ./build IRInterpreter optimized.ir < $2
fi
