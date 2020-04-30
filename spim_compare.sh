#!/bin/bash
if [[ $# -lt 1 ]]; then
	echo "[ ERROR ] Missing Arg:  .s filepath"
	exit 1
fi

TESTPATH="$1"
if [[ ! -f "$TESTPATH" ]]; then
	echo "[ ERROR ] Test File Does Not Exist:  $TESTPATH"
	exit 1
fi

# Strip file extension from the end
TESTNAME=${TESTPATH%"."*}
# Strip any directories in the path
TESTNAME=${TESTNAME##*"/"}

RESULTDIR="spim_results/"
if [[ ! -d "$RESULTDIR" ]]; then
	mkdir $RESULTDIR
fi

BASE_OUTPATH="mips_output/"$TESTNAME
RES_OUTPATH=$RESULTDIR$TESTNAME
N_OUTPATH=$BASE_OUTPATH"_naive.s"
N_RESPATH=$RES_OUTPATH"_naive.res"
I_OUTPATH=$BASE_OUTPATH"_intra.s"
I_RESPATH=$RES_OUTPATH"_intra.res"
DIFFPATH=$RES_OUTPATH".diff"

EXCPATH_PREFIX="/usr"
EXCPATH_SUFFIX="/share/spim/exceptions.s"
EXCOPT=""
if [[ ! -f "$EXCPATH_PREFIX$EXCPATH_SUFFIX" ]]; then
	ALTFILE="$EXCPATH_PREFIX/local$EXCPATH_SUFFIX"
	if [[ -f "$ALTFILE" ]]; then
		EXCOPT="-exception_file $ALTFILE"
	fi
fi

#./clean.sh && ./build.sh
BUILD_DIR=build/
SRC_FILE=sources.txt
if [[ -d "$BUILD_DIR" ]]; then
	rm -rf $BUILD_DIR
fi
mkdir $BUILD_DIR
rm -f **/*.class
rm -f src/**/**.class
find src -name "*.java" > $SRC_FILE
javac -d $BUILD_DIR @$SRC_FILE
rm $SRC_FILE

#./run.sh $TESTPATH -O0 $N_OUTPATH
java -cp $BUILD_DIR Assembler $TESTPATH -O0 $N_OUTPATH
spim -keepstats $EXCOPT -f $N_OUTPATH > $N_RESPATH

#./run.sh $TESTPATH -O1 $I_OUTPATH
java -cp $BUILD_DIR Assembler $TESTPATH -O1 $I_OUTPATH
spim -keepstats $EXCOPT -f $I_OUTPATH > $I_RESPATH

diff $N_RESPATH $I_RESPATH > $DIFFPATH
echo -e "\n\n\n"
clear && clear

BANNER="\n=======================================================\n"
SEP="__________________________________________________________\n"

echo -e "$SEP\nTEST NAME:  $TESTNAME\n(All test stats saved to $RES_OUTPATH.*)"
MSG="Register Allocation Performance Metrics (from spim-keepstats):\n"

echo -e "${BANNER}NAIVE $MSG"
# grep "#" $N_RESPATH
cat $N_RESPATH

echo -e "${BANNER}INTRA-BLOCK $MSG"
# grep "#" $I_RESPATH
cat $I_RESPATH

echo -e "$BANNER\nDiff:\n"
cat $DIFFPATH
echo -e "\n$SEP"
