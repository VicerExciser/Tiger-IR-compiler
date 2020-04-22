#!/bin/bash
if [[ $# -lt 1 ]]; then 
	IRFILE="example/example.ir"
else
	IRFILE="$1" 
fi

clear && ./clean.sh && ./build.sh && java -cp ./build IROptimizer $IRFILE  out.ir
