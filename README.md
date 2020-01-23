# Tiger-IR Helper Code & Interpreter

## Build

- Linux/macOS
```bash
mkdir build
find src -name "*.java" > sources.txt
javac -d build @sources.txt
```

- Windows CMD
```cmd
mkdir build
dir /A-D /B /S src\*.java > sources.txt
javac.exe -d build @sources.txt
```

## Run the Demo

```bash
java -cp ./build Demo example/example.ir out.ir
```

## Run the Interpreter

```bash
java -cp ./build IRInterpreter example/example.ir
```

The program will read input from the console and print output to the console.

If you have the input stored in a file, use this command to feed it to the program:

```bash
java -cp ./build IRInterpreter example/example.ir < example/example.in
```

And you can redirect the output to a file with this command:

```bash
java -cp ./build IRInterpreter example/example.ir < example/example.in > example.out
```
