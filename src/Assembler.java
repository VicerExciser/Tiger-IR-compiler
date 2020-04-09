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
			assembler = new Assembler(args[0]);
			assembler.run();
		} catch (StringIndexOutOfBoundsException sioobe) {
			System.out.println("[ERROR] Tiger-IR filename arg must end with suffix '.ir'");
			System.exit(1);
		}
		
	}

	public Assembler(String filename) throws StringIndexOutOfBoundsException, FileNotFoundException, IRException {
		IRReader reader = new IRReader();
		int start = filename.lastIndexOf('/');
		if (start < 0) start = 0;
		else start++;
		String outName = filename.substring(start, filename.indexOf(".ir"));

		inFile = reader.parseIRFile(filename);
		selector = new Selector();	// Instruction selector
		program = new MIPSFile(outName);	// Output MIPS/SPIM program file

	}

	public void run() throws Exception { //IRException {
		for (IRFunction function : inFile.functions) {

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