/*
Expects a command line argument: Tiger-IR file with extension ".ir"
*/
import ir.*;
import mips.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Assembler {

	private boolean PARSE_BY_FUNCTION = true;
	
	private IRProgram inFile;
	private Selector selector;
	private MIPSFile program;

	public static void main(String[] args) throws Exception {
		Assembler assembler;
		if (args.length < 1) {
			System.out.println("[ERROR] Missing Argument: Tiger-IR file with extension '.ir'");
			System.exit(1);
		} 
		try {
			if (args.length > 1) { // == 2) {
				assembler = new Assembler(args[0], args[1]);
			} else {
				assembler = new Assembler(args[0], null);
			}
			assembler.run();
		} catch (StringIndexOutOfBoundsException sioobe) {
			System.out.println("[ERROR] Tiger-IR filename arg must end with suffix '.ir'");
			System.exit(1);
		}
		
	}

	public Assembler(String filename, String outfilename) throws StringIndexOutOfBoundsException, 
															FileNotFoundException, IRException {
		IRReader reader = new IRReader();
		String outName;
		if (outfilename == null) {
			int start = filename.lastIndexOf('/') + 1;
			outName = filename.substring(start, filename.indexOf(".ir"));
		} else {
			outName = outfilename;
			if (!(".s".equals(outName.substring(outName.length() - 2)))) {
				System.out.println(outName.substring(outName.length() - 2));
				outName += ".s";
			}
		}

		inFile = reader.parseIRFile(filename);
		selector = new Selector();	// Instruction selector
		program = new MIPSFile(outName);	// Output MIPS/SPIM program file

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


		program.finalize();
		System.out.println("\n[ Assembler finished - program output to " + program.name + " ]");
	}

}