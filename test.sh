#!/bin/bash

clear && ./clean.sh && ./build.sh && java -cp ./build IROptimizer 
#example/example.ir out.ir
