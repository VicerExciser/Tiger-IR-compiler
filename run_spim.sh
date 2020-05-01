#!/bin/bash   
if [[ $# -lt 1 ]]; then
	echo "MISSING ARG: Must give filepath to a '.s' file"
	exit 1
fi
SFILE="$1"
spim -keepstats -exception_file /usr/local/share/spim/exceptions.s -f $SFILE

