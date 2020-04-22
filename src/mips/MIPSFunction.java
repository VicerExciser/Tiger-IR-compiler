package mips;

import mips.*;
import mips.operand.*;

import java.util.List;
// import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class MIPSFunction {
	
    // public boolean epilogueHandled;
    public int frameSize;
	public String name;
    public String returnType;

    public List<MIPSOperand> parameters;
    public List<MIPSOperand> variables;
    public List<MIPSInstruction> instructions;

    public Map<String, Addr> labelMap;
	public Map<String, String> irToMipsRegMap;
	public Map<String, Integer> assignments;

	public MIPSFunction(String name) {
		this(name, null, new LinkedList<>(), new LinkedList<>(), 
				new LinkedList<>());
	}

	public MIPSFunction(String name, 
						String returnType,
						List<MIPSOperand> parameters, 
						List<MIPSOperand> variables,
						List<MIPSInstruction> instructions) {
        // this.epilogueHandled = false;
        this.frameSize = 0;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.variables = variables;
        this.instructions = instructions;

        this.labelMap = new HashMap<>();
		this.irToMipsRegMap = new HashMap<>();
		this.assignments = new HashMap<>();

        // //// Generate function name label as first instruction
        // this.instructions.add(0, new MIPSInstruction(LABEL, name, null));

        // //// Generate first true instruction ("move $fp, $sp")
        // this.instructions.add(1, new MIPSInstruction(MOVE, null, 
        // 		new Register("$fp", false), new Register("$sp", false)));
    }
}