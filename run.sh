#!/bin/bash
RAMODE=0
if [[ $# -lt 1 ]]; then 
	# echo "MISSING ARG: Must specify input .ir file"
	# exit    
	IRFILE="src/test.ir" 
else
	IRFILE="$1" 
	if [[ $# -gt 1 ]]; then
		RAMODE="$2"
		if [[ (${#RAMODE} -gt 2 && ${RAMODE:0:2} == "-O") ]]; then
			# echo $RAMODE
			RAMODE=${RAMODE:2:1}
			# echo $RAMODE
		fi
	fi
fi

clear && ./clean.sh && ./build.sh

if [[ $# -eq 3 ]]; then
	java -cp ./build Assembler $IRFILE -O$RAMODE $3
else
	java -cp ./build Assembler $IRFILE -O$RAMODE
fi
