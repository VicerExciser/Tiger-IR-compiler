#!/bin/bash
if [[ $# -lt 1 ]]; then 
	# echo "MISSING ARG: Must specify input .ir file"
	# exit
	IRFILE="src/test.ir" 
	##IRFILE="example/example.ir"
else
	IRFILE="$1" 
fi

clear && ./clean.sh && ./build.sh && java -cp ./build Assembler $IRFILE 
##clear && ./clean.sh && ./build.sh && java -cp ./build IROptimizer $IRFILE  out.ir
