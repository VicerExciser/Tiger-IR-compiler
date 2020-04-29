package mips;

import mips.*;
import mips.cfg.*;
import mips.operand.*;

import ir.cfg.*;

import java.util.List;
// import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class MIPSFunction {
	
    // public boolean epilogueHandled;
    public int frameSize;
	public String name;
    public String returnType;

    public List<MIPSOperand> parameters;
    public List<MIPSOperand> variables;
    /*public*/ List<MIPSInstruction> instructions;
    public MIPSCFG cfg;

    public Map<String, Addr> labelMap;
	public Map<String, String> irToMipsRegMap;
	public Map<String, Integer> assignments;

    private int blockTrackingNumber;

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

        blockTrackingNumber = 0;

        // //// Generate function name label as first instruction
        // this.instructions.add(0, new MIPSInstruction(LABEL, name, null));

        // //// Generate first true instruction ("move $fp, $sp")
        // this.instructions.add(1, new MIPSInstruction(MOVE, null, 
        // 		new Register("$fp", false), new Register("$sp", false)));
    }


    public String getVarNameForRegister(Register varReg) {
        return getVarNameForRegister(varReg.name);
    }

    public String getVarNameForRegister(String varRegName) {
        //// Attempt a reverse lookup in the function's irToMipsRegMap mappings
        String variableName = null;
        if (irToMipsRegMap.containsValue(varRegName)) {
            for (String key : irToMipsRegMap.keySet()) {
                if (varRegName.equals(irToMipsRegMap.get(key))) {
                    variableName = key;
                    break;
                }
            }
        }
        return variableName;
    }


    /* i.e., blocks for the function "main" can be identified as
    "main_B0", "main_B1", "main_B2", etc.
    */
    public String getUniqueBlockID() {
        String blockID = this.name + "_B" + String.valueOf(blockTrackingNumber);
        blockTrackingNumber++;
        return blockID;
    }

    public MIPSBlock getBlockWithID(String blockID) {
        return this.cfg.getBlockWithID(blockID);
    }

    public List<MIPSInstruction> getInstructions() {
        return this.instructions;
    }

    public MIPSBlock getCurrentBlock() {
        MIPSBlock curBlock = null;
        try {
            curBlock = ((MIPSInstruction) (((LinkedList) (this.instructions)).getLast())).parentBlock;
        } catch (NoSuchElementException nsee) {   //// Indicative of an empty list of instructions
            for (MIPSBlock block : this.cfg.blocks) {
                curBlock = block;
            }
        }
        return curBlock;
    }

    public void addInstructionToCurrentBlock(MIPSInstruction inst) {
        MIPSBlock curBlock = getCurrentBlock();
        curBlock.appendInstruction(inst);
        this.instructions.add(inst);
    }

    public void addInstructionsToCurrentBlock(List<MIPSInstruction> parsedInst) {
        addInstructionsToBlock(getCurrentBlock(), parsedInst);
    }

    public void addInstructionsToBlockID(String blockID, List<MIPSInstruction> parsedInst) {
        addInstructionsToBlock(getBlockWithID(blockID), parsedInst);
    }

    public void addInstructionsToBlock(MIPSBlock block, List<MIPSInstruction> parsedInst) {
        if (block == null) {
            System.out.println("[MIPSFunction::addInstructionsToBlock] ERROR: MIPSBlock instance cannot be null!");
            return;
        }

        for (MIPSInstruction inst : parsedInst) {
            block.appendInstruction(inst);
            this.instructions.add(inst);
        }
    }

    public void addBlock(MIPSBlock block) {
/*
        if (this.cfg.blocks.isEmpty()) {
            this.cfg.entryNode = block;
        }
        this.cfg.blocks.add(block);
        this.cfg.irBlockMap.put(block.associatedIRBlock, block);
*/
        this.cfg.registerBlock(block);
    }

    public MIPSBlock getAssociatedBlock(BasicBlockBase irBlock) {
        return this.cfg.getBlockWithAssociatedIRBlock(irBlock);
    }


    public void printRegisterMapping() {
        String banner = "\n________________________________\n";
        System.out.println(banner + " Register Mapping for Function "+name+":\n");
        for (String key : this.irToMipsRegMap.keySet()) {
            // System.out.println("\t"+key+"  <--->  "+irToMipsRegMap.get(key).toString());
            System.out.println("\t"+irToMipsRegMap.get(key).toString()+"  <--->  "+key);
        }
        System.out.println(banner);
    }

}
