IF NOT EXIST %~dp0build\ GOTO NODIR
GOTO DIR


:NODIR
mkdir build

:DIR
dir /A-D /B /S src\*.java > sources.txt
javac.exe -d build @sources.txt
