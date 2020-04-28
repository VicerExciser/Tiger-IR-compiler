/*
- Output file extension is .s for the SPIM simulator
- Program file layout will be composed of the following sections:
	1) .data - For variable declarations (MIPSData.java)
	2) .text - Block containing instructions (MIPSText.java)
*/
package mips;

import ir.*;
import mips.*;
import mips.cfg.*;
import mips.operand.*;
import mips.MIPSOp.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MIPSFile {

	public String name;
	// public Map<String, 
	public List<MIPSFunction> functions;

	public Map<Integer, MIPSInstruction> text;
	// public Map<Integer, Integer> data;
	public Map<String, Integer> labels;

	private int lineCount;
	private String shortFileName;

	public MIPSFile() {
		this("MIPS_output.s");
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
		this.shortFileName = this.name;
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
		this.functions.add(function);
		// for (MIPSInstruction instruction : function.getInstructions()) {
		//		append(instruction);
		// }
		for (MIPSBlock block : function.cfg.blocks) {

			//// FOR DEBUG
			String prefix = " ---- { BLOCK  '" + block.id + "'  ";
			String suffix = " } ---- ";
			append(new MIPSInstruction(MIPSOp.COMMENT, 
					prefix + "BEGIN" + suffix,
					(MIPSOperand[]) null));
			//// FOR DEBUG

			for (MIPSInstruction instruction : block.instructions) {
				append(instruction);
			}

			//// FOR DEBUG
			append(new MIPSInstruction(MIPSOp.COMMENT, 
					prefix + "END" + suffix,
					(MIPSOperand[]) null));
			//// FOR DEBUG
		}

		//// End of program (i.e., main function) must make 'exit' syscall
		if ("main".equals(function.name.toLowerCase())) {
			append(new MIPSInstruction(MIPSOp.COMMENT, "Program exit", (MIPSOperand[]) null));
			MIPSOperand[] exitOperands = {new Register("$v0", false), new Imm("10", "DEC")};
			append(new MIPSInstruction(MIPSOp.LI, null, exitOperands));	// li $v0, 10
			append(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand[]) null));  // syscall
		}

		//// Add space to printed following the end of each function
		append(new MIPSInstruction(MIPSOp.NOP, null, (MIPSOperand[]) null));
	}


	public void finalizeProgramFile() throws FileNotFoundException, IOException {
		//// Ensure that output path exists (creating all dirs on the output path if not)
		// Path outPath = Paths.get(this.name);
		// if (Files.notExists(dirPath) || !Files.isDirectory(dirPath)) {
		// 	System.out.println("[MIPSFile::finalizeProgramFile] ERROR: Path '"+pathPrefix+"' does not exist; creating now");
		// 	Files.createDirectories(dirPath);
		// }
		validatePath(this.name);

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


	private void validatePath(String path) throws IOException {
		Path outPath = Paths.get(path);

		if (Files.notExists(outPath)) {  // || !Files.isDirectory(dirPath)) {
			// System.out.println("[MIPSFile::validatePath] ERROR: Path '"+path+"' does not exist; creating now!");
			
			if (outPath.getFileName().toString().indexOf(".s") >= 0) {
				// int nameCount = outPath.getNameCount();
				// outPath = outPath.subpath(0, nameCount-1);
				outPath = outPath.getParent();	//// equivalent to:  subpath(0, getNameCount()-1);
			}

			Files.createDirectories(outPath);
		}
	}


	public void printLabels() {
		for (String label : labels.keySet()) {
			System.out.println(label + " -> " + Integer.toHexString(labels.get(label)));
		}
	}


	public void addFullPathForOutput(String pathPrefix) throws IOException {
		//// First check if directory path exists; create it if not
		Path dirPath = Paths.get(pathPrefix);
		// if (Files.notExists(dirPath) || !Files.isDirectory(dirPath)) {
		// 	// System.out.println("[MIPSFile::addFullPathForOutput] ERROR: Path '"+pathPrefix+"' does not exist; creating now");
		// 	Files.createDirectories(dirPath);
		
		validatePath(pathPrefix);

		//// Redundant double-check that necessary directories on output path definitely exist
			if (Files.notExists(dirPath) || !Files.isDirectory(dirPath)) {
				System.out.println("[MIPSFile::addFullPathForOutput] ERROR: Path '"+pathPrefix+"' STILL does not exist after creation....");
			}
		// }

		this.name = pathPrefix + System.getProperty("file.separator") + this.name;

/*
		if (!this.shortFileName.equals(Paths.get(this.name).getFileName().normalize().toString())) {
			System.out.println("[MIPSFile::addFullPathForOutput] ERROR: Path.getFileName() doesn't equal this.shortFileName!");
			System.out.println("  --->  shortFileName = '" + this.shortFileName + "'\n  --->  getFileName() = '"
					+ Paths.get(this.name).getFileName().normalize().toString() + "'\n");
		}
*/
	}

}
