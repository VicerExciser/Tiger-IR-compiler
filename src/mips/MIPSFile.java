/*
- Output file extension is .s for the SPIM simulator
- Program file layout will be composed of the following sections:
	1) .data - For variable declarations (MIPSData.java)
	2) .text - Block containing instructions (MIPSText.java)
*/
package mips;

import ir.*;
import mips.*;
import mips.operand.*;
import mips.MIPSOp.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class MIPSFile {

	public String name;
	// public Map<String, 
	public List<MIPSFunction> functions;

	public Map<Integer, MIPSInstruction> text;
	// public Map<Integer, Integer> data;
	public Map<String, Integer> labels;

	private int lineCount;

	public MIPSFile() {
		this("output");
	}

	public MIPSFile(String filename) {
		this(filename,
				new ArrayList<MIPSFunction>(),
				new HashMap<Integer, MIPSInstruction>(), 
				// new HashMap<Integer, Integer>(),
				new HashMap<String, Integer>());
	}

	public MIPSFile(String filename, List<MIPSFunction> functions,
				Map<Integer, MIPSInstruction> text,
				// Map<Integer, Integer> data, 
				Map<String, Integer> labels) {
		this.name = filename;
		if (this.name.indexOf(".s") < 0) {
			this.name += ".s";
		}
		this.functions = functions;
		this.text = text;
		// this.data = data;
		this.labels = labels;

		lineCount = 0;

		// Assuming NO DATA SECTION -- ONLY .text (INSTRUCTIONS):
		append(new MIPSInstruction(MIPSOp.DIRECTIVE, ".text", (MIPSOperand[]) null));
	}

	public void append(MIPSInstruction line) {
		this.text.put(++lineCount, line);
		//// FOR DEBUG
		System.out.format("[ %-3d ]  %s%n", lineCount, line.toString());
	}

	public void addFunction(MIPSFunction function) {
		for (MIPSInstruction instruction : function.instructions) {
			append(instruction);
		}

		//// End of program (i.e., main function) must make 'exit' syscall
		if ("main".equals(function.name.toLowerCase())) {
			MIPSOperand[] exitOperands = {new Register("$v0", false), new Imm("10", "DEC")};
			append(new MIPSInstruction(MIPSOp.LI, null, exitOperands));	// li $v0, 10
			append(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand[]) null));  // syscall
		}

		//// Add space to printed following the end of each function
		append(new MIPSInstruction(MIPSOp.NOP, null, (MIPSOperand[]) null));
	}

	public void finalize() throws FileNotFoundException {
		//// Write the actual file contents
        PrintStream filePrinter = new PrintStream(this.name);
        // for (MIPSInstruction instruction : )
        // for (int i = 1; i <= this.text.size(); i++) {
        for (int i = 1; i <= lineCount; i++) {
        	filePrinter.println(this.text.get(i).toString());
        }
        //// Leave a blank line after the last instruction to make SPIM happy
        filePrinter.println();
	}

	public void printLabels() {
		for (String label : labels.keySet()) {
			System.out.println(label + " -> " + Integer.toHexString(labels.get(label)));
		}
	}


}