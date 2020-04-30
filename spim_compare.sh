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

./run.sh $TESTPATH -O0 $N_OUTPATH
#./run_spim.sh $N_OUTPATH > $N_RESPATH
spim -keepstats $EXCOPT -f $N_OUTPATH > $N_RESPATH

./run.sh $TESTPATH -O1 $I_OUTPATH
#./run_spim.sh $I_OUTPATH > $I_RESPATH
spim -keepstats $EXCOPT -f $I_OUTPATH > $I_RESPATH

diff $N_RESPATH $I_RESPATH > $DIFFPATH
clear && clear

BANNER="\n=======================================================\n"
SEP="__________________________________________________________\n"

echo -e "$SEP\nTEST NAME:  $TESTNAME\n(All test stats saved to $RES_OUTPATH.*)"
MSG="Register Allocation Performance Metrics (from spim-keepstats):\n"

echo -e "${BANNER}NAIVE $MSG"
grep "#" $N_RESPATH

echo -e "${BANNER}INTRA-BLOCK $MSG"
grep "#" $I_RESPATH

echo -e "$BANNER\nDiff:\n"
cat $DIFFPATH
echo -e "\n$SEP"
