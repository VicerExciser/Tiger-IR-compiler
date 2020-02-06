package ir;

import ir.datatype.*;
import ir.operand.*;
import ir.cfg.BasicBlockBase;
import ir.cfg.MinBasicBlock;
import ir.IRInstruction.OpCode.*;
import ir.IRInstruction;

import java.util.Comparator;
import java.util.Arrays;
import java.util.List;

/*
	Utility class to host helpful functions for classification and
	analysis of IR instructions, functions, etc...
*/
public class IRUtil {

	public static class InstructionComparator implements Comparator<IRInstruction> {
		@Override
        public int compare(IRInstruction instA, IRInstruction instB) {
            return instA.irLineNumber - instB.irLineNumber;
        }
    }

    public static class BlockComparator implements Comparator<BasicBlockBase> {
    	@Override
        public int compare(BasicBlockBase bbA, BasicBlockBase bbB) {
            return bbA.compareTo(bbB);
        }
    }

	public static boolean isDefinition(IRInstruction inst) {
		return  inst.opCode == IRInstruction.OpCode.ADD 
            ||  inst.opCode == IRInstruction.OpCode.SUB 
            ||  inst.opCode == IRInstruction.OpCode.MULT
            ||  inst.opCode == IRInstruction.OpCode.DIV
            ||  inst.opCode == IRInstruction.OpCode.AND
            ||  inst.opCode == IRInstruction.OpCode.OR
            ||  inst.opCode == IRInstruction.OpCode.CALLR
            ||  inst.opCode == IRInstruction.OpCode.ASSIGN
            ||  inst.opCode == IRInstruction.OpCode.ARRAY_LOAD;
	}

	public static boolean isDefinition(MinBasicBlock block) {
		return isDefinition(block.s);
	}

	public static boolean isConditionalBranch(IRInstruction inst) {
		return  inst.opCode == IRInstruction.OpCode.BREQ 
            ||  inst.opCode == IRInstruction.OpCode.BRNEQ 
            ||  inst.opCode == IRInstruction.OpCode.BRLT
            ||  inst.opCode == IRInstruction.OpCode.BRGT
            ||  inst.opCode == IRInstruction.OpCode.BRLEQ
            ||  inst.opCode == IRInstruction.OpCode.BRGEQ;
	}

	public static boolean isConditionalBranch(MinBasicBlock block) {
		return isConditionalBranch(block.s);
	}

	static List<IRInstruction.OpCode> criticalOps = Arrays.asList(
        IRInstruction.OpCode.BREQ, 
        IRInstruction.OpCode.BRNEQ, 
        IRInstruction.OpCode.BRLT, 
        IRInstruction.OpCode.BRGT, 
        IRInstruction.OpCode.BRLEQ, 
        IRInstruction.OpCode.BRGEQ,
        IRInstruction.OpCode.GOTO,
        IRInstruction.OpCode.RETURN,
        IRInstruction.OpCode.CALL, 
        IRInstruction.OpCode.CALLR,
        IRInstruction.OpCode.ARRAY_STORE,
        IRInstruction.OpCode.LABEL
    );

	public static boolean isCritical(IRInstruction inst) {
		/* Critical instructions include:
	         - all branches
	         - GOTO
	         - ARRAY_STORE
	         - array assignment (ASSIGN)
	         - CALL
	         - CALLR
	         - RETURN
	         - LABEL
		*/
        return (inst.opCode == IRInstruction.OpCode.ASSIGN) 
	        	? isArrayAssignment(inst)
	        	: criticalOps.contains(inst.opCode);
	}

	public static boolean isCritical(MinBasicBlock block) {
		return isCritical(block.s);
	}

	public static boolean isArrayAssignment(IRInstruction inst) {
        // The first operand must be an array for an array assignment
        if (inst != null && inst.operands != null && inst.operands.length > 0) 
        {
            IROperand operand = inst.operands[0];
            // The second operand must be an int type, and represents the size
            int size;
            try {
            	size = Integer.parseInt(inst.operands[1].toString());
			}
            catch (java.lang.NumberFormatException nfe) {
            	size = 1;
			}
            if (operand instanceof IRVariableOperand || operand instanceof IRConstantOperand) 
            {
                // I'm unsure which of these conditions accurately determine whether
                // the operand is an Array or not...
                boolean isArray1 = ((IRVariableOperand)operand).type instanceof IRArrayType;
                boolean isArray2 = ((IRVariableOperand)operand).type == IRArrayType.get(IRIntType.get(), size);
                				// || ((IRConstantOperand)operand).type == IRArrayType.get(IRIntType.get(), 1);
                boolean isArray3 = ((IRVariableOperand)operand).type == IRArrayType.get(IRFloatType.get(), size);
                				// || ((IRConstantOperand)operand).type == IRArrayType.get(IRFloatType.get(), 1);

                // ...so I'm just gonna check all of them (for now).
                return isArray1 || isArray2 || isArray3;
            }
        }
        return false;
    }

    // Determines whether the instruction's opCode is a branch, goto, or return statement
    public static boolean isControlFlow(IRInstruction inst) {
		for (int i = 0; i < 8; i++) {
			if (inst.opCode == criticalOps.get(i)) return true;
		}
		return false;
	}

	public static boolean isControlFlow(MinBasicBlock block) {
		return isControlFlow(block.s);
	}

    public static void identifyLeaders(IRFunction f) {
    	for (int i = 0; i < f.instructions.size(); i++) {
    		/*	Leaders (first instruction of a basic block) satisfy at least one of the following:
				1. the first instruction in the procedure/function
				2. the target of a 'goto' or 'branch' instruction (i.e., OpCode is LABEL)
				3. the successor of a 'branch' instruction
			*/
			if (i == 0)
				f.instructions.get(i).isLeader = true;
			// Mark the instruction immediately AFTER a label or branch operation as a leader 
			// and bump i to skip over its index
			else if ((i < f.instructions.size()-1) 		// Bounds check to make sure [i+1] is in range
					&& (f.instructions.get(i).opCode == IRInstruction.OpCode.LABEL
					||	f.instructions.get(i).opCode == IRInstruction.OpCode.BREQ
        			||  f.instructions.get(i).opCode == IRInstruction.OpCode.BRNEQ
        			||  f.instructions.get(i).opCode == IRInstruction.OpCode.BRLT 
        			||  f.instructions.get(i).opCode == IRInstruction.OpCode.BRGT 
        			||  f.instructions.get(i).opCode == IRInstruction.OpCode.BRLEQ
        			||  f.instructions.get(i).opCode == IRInstruction.OpCode.BRGEQ))
				f.instructions.get(++i).isLeader = true;
			else
				f.instructions.get(i).isLeader = false;
    	}
    }

    public static IRInstruction getInstructionAtLine(IRFunction f, int line) {
    	for (IRInstruction i : f.instructions) {
    		if (i.irLineNumber == line) return i;
    	}
    	return null;
    }

    public static IRInstruction getInstructionBeforeThis(IRFunction f, IRInstruction inst) {
    	int curIdx = f.instructions.indexOf(inst);
    	if (curIdx < 1) return null;	// Return null if inst not found or is first instruction
    	return f.instructions.get(curIdx-1);
    }

    public static IRInstruction getInstructionBeforeThis(IRInstruction inst) {
    	if (inst.belongsToBlock != null)
    		return getInstructionBeforeThis(inst.belongsToBlock.parent, inst);
    	return null;
    }

    public static IRInstruction getInstructionAfterThis(IRFunction f, IRInstruction inst) {
    	int curIdx = f.instructions.indexOf(inst);
    	if (curIdx == -1 || curIdx == f.instructions.size()-1) 	// Return null if inst not found
    		return null;										// or is last instruction
		return f.instructions.get(curIdx+1);
    }

    public static IRInstruction getInstructionAfterThis(IRInstruction inst) {
    	if (inst.belongsToBlock != null)
    		return getInstructionAfterThis(inst.belongsToBlock.parent, inst);
    	return null;
    }

    public static IRInstruction getLabelTarget(IRFunction f, IRLabelOperand label) {
    	for (IRInstruction i : f.instructions) {
    		if (i.opCode == IRInstruction.OpCode.LABEL) {
    			if (((IRLabelOperand)i.operands[0]).getName().equals(label.getName()))
    				return i;
    		}
    	}
    	return null;
    }

}










