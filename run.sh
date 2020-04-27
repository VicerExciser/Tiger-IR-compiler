#!/bin/bash
RAMODE=0
if [[ $# -lt 1 ]]; then 
	# echo "MISSING ARG: Must specify input .ir file"
	# exit
	IRFILE="src/test.ir" 
	##IRFILE="example/example.ir"
else
	IRFILE="$1" 
	if [[ $# -gt 1 ]]; then
		RAMODE="$2"
	fi
fi

clear && ./clean.sh && ./build.sh

if [[ $# -eq 3 ]]; then
	java -cp ./build Assembler $IRFILE $RAMODE $3
else
	java -cp ./build Assembler $IRFILE $RAMODE
fi
