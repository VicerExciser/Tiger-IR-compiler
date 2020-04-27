/**		Expected Command Line Arguments:

- Required command line argument (args[0]): 
	Absolute or relative path to an input Tiger-IR file with extension ".ir"

- Optional command line argument (args[1]): 
	"-O" followed immediately by an integer (0 or 1) to specify a register allocation mode:
	 	-->		"-O0" to enable NAIVE allocation (default setting), or
	 	-->		"-O1" to enable INTRA-BLOCK register allocation.

	^ NOTE: The "-O" prefix is optional (can just pass the integer 0 or 1) for the second arg
	^ NOTE: The second arg for mode is actually entirely optional, as "-O0" will be used by default

- Optional command line argument (args[2]): 
	An output file name/path for the resulting MIPS program to be written to

	^ NOTE: If no output filename/path is given, the output filename will be the same as the input
			filename with the ".ir" extension replaced by ".s"
	^ NOTE: By default, output files will be saved to a folder called "mips_output/" relative
			to the Tiger-IR-compiler project root directory (i.e., "Tiger-IR-compiler/mips_output/");
			this default output directory will be automatically created if it does not exist


How To Build Project in 3 Different Ways (from Tiger-IR-compiler project root directory):

	1)	mkdir build
		find src -name "*.java" > sources.txt
		javac -d build @sources.txt

	2)	./build.sh

	3) 	./run.sh  (will call build.sh)


Synopsis:
	
	java -cp ./build Assembler <input/IR/filepath> [-O<register allocation mode>] [optional/output/filepath]
	java -cp ./build Assembler [yourInputFilePath].ir -O[0 | 1] [pathForOutputFile].s 


How To Run in 5 Different Ways (from Tiger-IR-compiler project root directory):

	1)	java -cp ./build Assembler input_file_path.ir -O0 output_file_path.s

	2)	./run.sh test_cases/quicksort/quicksort.ir

	3)	./run.sh test_cases/quicksort/quicksort.ir 1

	4)	./run.sh test_cases/quicksort/quicksort.ir -O0

	5)	./run.sh test_cases/quicksort/quicksort.ir -O1 output_dir/mips_quicksort.s
**/
import ir.*;
import mips.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Assembler {

	private static boolean REQUIRE_ALLOC_MODE_ARG = true; //false;

	private boolean PARSE_BY_FUNCTION = true;
	private boolean FULL_PATH_OUTPUT_FILE = true;
	private boolean USE_DEFAULT_OUTPUT_DIR = true;

	private String PATH_DELIM = System.getProperty("file.separator"); 	//// This is "/" on UNIX and "\" on Windows

	private String defaultOutDir = "mips_output";  //"test_files";
	private String projectRootPath;
	private Path currentWorkingDir;
	
	private IRProgram inFile;
	private Selector selector;
	private MIPSFile program;
	

	public static void main(String[] args) throws Exception {

		Assembler assembler;

		if (args.length < 1) {
			System.out.println("[ERROR] Missing Argument #0: Tiger-IR file with extension '.ir'\n(Assembler.java)");
			System.exit(1);
		} else if (REQUIRE_ALLOC_MODE_ARG) {
			String supportedModesMsg = "\n Supported mode values  -->  { 0 = NAIVE, 1 = INTRABLOCK }\n (Assembler.java)";
			if (args.length < 2) {
				System.out.println("[ERROR] Missing Argument #1: Register allocation mode"+supportedModesMsg);
				System.exit(1);
			}
			//// Ensure args[1] is numeric
			try {
				Integer num = Integer.parseInt(args[1].replaceAll("-O",""));
			} catch (NumberFormatException nfe) {
				System.out.println("[ERROR] Invalid Argument #1: Register allocation mode must be an integer (given: '"+args[1]+"')"+supportedModesMsg);
				System.exit(1);
			}
		}

		try {

			//// FOR DEBUG
			System.out.println("USER:         "+System.getProperty("user.name"));
			System.out.println("HOME:         "+System.getProperty("user.home"));
			System.out.println("CWD:          "+System.getProperty("user.dir"));	//// prints the path of the current working directory
			System.out.println("OS:           "+System.getProperty("os.name"));	//// prints the name of the Operating System
			System.out.println("JRE Version:  "+System.getProperty("java.runtime.version"));	//// prints Java Runtime Version

			System.out.println("\n[Assember::main] args.length = " + String.valueOf(args.length));
			for (int i = 0; i < args.length; i++) {
				System.out.println(" -->  args["+String.valueOf(i)+"] = " + args[i]);
			}
			System.out.println();
			//// FOR DEBUG

			if (args.length > 1) {
				String outfile = null;
				if (args.length > 2) {
					outfile = args[2];
				}
				assembler = new Assembler(args[0], Integer.parseInt(args[1].replaceAll("-O","")), outfile);
			} else {
				assembler = new Assembler(args[0], 0, null);
			}
			assembler.run();
		} catch (StringIndexOutOfBoundsException sioobe) {
			System.out.println("[ERROR] Tiger-IR filename arg #0 must end with suffix '.ir'\n(Assembler.java)");
			System.exit(1);
		}
		
	}

	public Assembler(String filename, int allocMode, String outfilename) throws FileNotFoundException, 
											StringIndexOutOfBoundsException, IOException, IRException {
		IRReader reader = new IRReader();

		// projectRootPath = System.getProperty("user.dir");	//// Returns the current working directory
		currentWorkingDir = Paths.get("").toAbsolutePath();
		projectRootPath = currentWorkingDir.normalize().toString();

		String outName;
		if (outfilename == null) {
			// int start = filename.lastIndexOf('/') + 1;
			int start = filename.lastIndexOf(PATH_DELIM) + 1;
			outName = filename.substring(start, filename.indexOf(".ir"));
		} else {
			outName = outfilename;
			// if (!(".s".equals(outName.substring(outName.length() - 2)))) {
				// System.out.println(outName.substring(outName.length() - 2));
			if (outfilename.indexOf(".s") < 0) {
				outName += ".s";
			}
			USE_DEFAULT_OUTPUT_DIR = false;
		}

		inFile = reader.parseIRFile(filename);
		selector = new Selector(allocMode);	// Instruction selector
		program = new MIPSFile(outName);	// Output MIPS/SPIM program file

		if (FULL_PATH_OUTPUT_FILE) {
			if (USE_DEFAULT_OUTPUT_DIR) {
				String outPath = projectRootPath + PATH_DELIM + defaultOutDir;
				program.addFullPathForOutput(outPath);
			} else {
				program.addFullPathForOutput(projectRootPath);
			}
		}

		//// FOR DEBUG
		System.out.println("[Assember::Constructor] in-filename: "+filename+",\n  out-filename: "+program.name);
		//// FOR DEBUG

	}

	public void run() throws Exception { //IRException {
		//// Parse the main function first
		IRFunction mainFunction = null;
		for (IRFunction function : inFile.functions) {
			if ("main".equalsIgnoreCase(function.name)) {
				mainFunction = function;
				break;
			}
		}
		if (mainFunction == null || !inFile.functions.remove(mainFunction)) {
			System.out.println("[ERROR] Assembler could not find a function named 'main' in the IR program!");
			System.exit(1);
		}
		else if (PARSE_BY_FUNCTION) {
			MIPSFunction newFunction = selector.parseFunction(mainFunction);
			program.addFunction(newFunction);

			//// FOR DEBUG
			newFunction.printRegisterMapping();
		}

		for (IRFunction function : inFile.functions) {
			if (function.name.equalsIgnoreCase("main")) {
				//// Should never occur...
				System.out.println("-- Assembler failed to remove main function from IR program after processing");
				continue;
			}

			if (PARSE_BY_FUNCTION) {
				MIPSFunction newFunction = selector.parseFunction(function);
				program.addFunction(newFunction);

				//// FOR DEBUG
				newFunction.printRegisterMapping();
			}
			else {
				for (IRInstruction oldInst : function.instructions) {
					// Translate old Tiger-IR instruction to new MIPS/SPIM counterpart(s)
					for (MIPSInstruction newInst : selector.parseInstruction(oldInst, function.name)) {
						// Append line to the resultant MIPS program file
						program.append(newInst);
					}
				}
			}
		}


		program.finalizeProgramFile();

		Path progPath = Paths.get(program.name);  //.toAbsolutePath().normalize();
		int pathCount = progPath.getNameCount();
		int subpathIdxOffset = USE_DEFAULT_OUTPUT_DIR ? 3 : 2;
		String outPathStr = progPath.subpath(pathCount-subpathIdxOffset, pathCount).toString();

		System.out.println("\n[ Assembler finished - program written to '" + outPathStr + "' ]");
	

		//// FOR DEBUG
		// for (MIPSFunction func : program.functions) {
		// 	func.printRegisterMapping();
		// }

	}

}