import ir.*;
import mips.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Tester {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("[ERROR] Missing Argument: Tiger-IR file with extension '.ir'");
			System.exit(1);
		}
		String filename = args[0];
		String replacement = replace(args[0]);
		System.out.println("Input: "+filename+"\nOutput: "+replacement);

		IRReader reader = new IRReader();
		try {
			IRProgram program = reader.parseIRFile(filename);
			
			// System.out.println("\n\n- printProgram():");
			// printContents(program, true, false);
			// System.out.println("\n\n- printFunctions():");
			// printContents(program, false, true);
			System.out.println("\n\n- INDIVIDUAL:");
			printContents(program, false, false);
			
			// for (IRFunction function : program.functions) {
			// 	System.out.println(function.name);
			// }
		} catch (FileNotFoundException fnfe) {
			System.exit(0);
		}
	}

	public static String replace(String in) {
		int start = in.lastIndexOf('/');
		if (start < 0) start = 0;
		else start++;
		String out = in.substring(start, in.indexOf(".ir"));
		if (out.indexOf(".s") < 0) {
			out += ".s";
		}
		return out;
	}

	public static void printContents(IRProgram program, boolean printProgram, boolean printFunctions) {
		IRPrinter printer = new IRPrinter(System.out);
		if (printProgram) {
			printer.printProgram(program);
			return;
		}
		for (IRFunction function : program.functions) {
			System.out.println("\n"+function.name);
			IRFunction parentFunction = function;
			if (printFunctions) {
				printer.printFunction(parentFunction);
			} else {
				for (IRInstruction oldInst : parentFunction.instructions) {
					printer.printInstruction(oldInst);
				}
			}
		}
	}
}