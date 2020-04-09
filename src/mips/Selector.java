/*
The first step is to build an instruction selector that transforms Tiger-IR 
instructions to MIPS3 instructions that operate on an unlimited number of 
virtual registers. 

It is fine if the instructions generated by your project include pseudo 
instructions supported by SPIM. The main criterion is that the code that 
you generate must be executable on the SPIM simulator. 

It will suffice to implement a simple instruction selector that translates 
one IR instruction at a time. Also, note that all the intrinsic functions 
in Tiger-IR can be implemented using SPIM system calls.
*/

import ir.*;
import ir.datatype.*;
import ir.operand.*;
import mips.*;
import mips.operand.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;


public class Selector {

	public Map<String, Addr> labelMap;
	public Map<String, Register> regs;
	public Map<String, String> irToMipsRegMap;
	public Map<String, Integer> assignments;

	private String[] tempRegNames = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"};

	public Selector() { 
		labelMap = new HashMap<>();
		regs = new HashMap<>();
		irToMipsRegMap = new HashMap<>();

		initializeRegisters();
	}

	private Register getMappedReg(String operand) {
		String associatedRegName = null;
		if (irToMipsRegMap.containsKey(operand)) {
			associatedRegName = irToMipsRegMap.get(operand);
			// return regs.get(associatedRegName);
		}
		else {
			//// Find an unused temporary register to map the operand/variable name to
			for (String name : tempRegNames) {
				if (!irToMipsRegMap.containsValue(name)) {
					associatedRegName = name;
					irToMipsRegMap.put(operand, associatedRegName);
					break;
				}
			}
		}

		if (associatedRegName == null) {
			System.out.println("[getMappedReg] ERROR: Could not assign a temporary register to operand '"+operand+"'");
			return null;
		}
		return regs.get(associatedRegName);
	}

	public MIPSFunction parseFunction(IRFunction irFunction) {
		MIPSFunction mipsFunction = new MIPSFunction(irFunction.name);
		labelMap.put(irFunction.name, new Addr(irFunction.name));

		//// Generate function name label as first instruction
        mipsFunction.instructions.add(0, new MIPSInstruction(MIPSOp.LABEL, irFunction.name, (MIPSOperand[]) null));
        //// Generate first true instruction ("move $fp, $sp")
        mipsFunction.instructions.add(1, new MIPSInstruction(MIPSOp.MOVE, null, 
        		regs.get("$fp"), regs.get("$sp")));

		if (irFunction.returnType != null) {
			mipsFunction.returnType = irFunction.returnType.toString();
		}

		if (!irFunction.parameters.isEmpty()) {
			for (IRVariableOperand irParam : irFunction.parameters) {
				MIPSOperand mipsParam = null;

				// ...	//// TODO

				mipsFunction.parameters.add(mipsParam);
			}
		}

		if (!irFunction.variables.isEmpty()) {
			for (IRVariableOperand irVar : irFunction.variables) {
				MIPSOperand mipsVar = null;

				// ...	//// TODO

				mipsFunction.variables.add(mipsVar);
			}
		}

		//// TODO
		/*
			Must compute the size of params & vars for the function
			then generate instructions to reserve stack space:
				"addi $sp, $sp, -104"
		*/
		//// TODO

		for (IRInstruction irInst : irFunction.instructions) {
			List<MIPSInstruction> parsedInst = parseInstruction(irInst, irFunction.name);
			// for (MIPSInstruction mipsInst : parsedInst) {
			// 	mipsFunction.instructions.add(mipsInst);
			// }
			mipsFunction.instructions.addAll(parsedInst);
		}

		return mipsFunction;
	}


	public List<MIPSInstruction> parseInstruction(IRInstruction irInst, String parentName) {
		List<MIPSInstruction> parsedInst = new LinkedList<>();

		MIPSInstruction mipsInst;
		MIPSOp op = null;
		String label = null;
		MIPSOperand[] mipsOperands = null;// = new MIPSOperand[irInst.operands.length];

		switch(irInst.opCode) {
			case LABEL:
				label = new String(irInst.operands[0].toString() + "_" + parentName);
				op = MIPSOp.LABEL;
				parsedInst.add(new MIPSInstruction(op, label, mipsOperands));
				labelMap.put(label, new Addr(label));
				// return parsedInst;
				break;

			case ASSIGN:
				// operand[0] will be a register/variable,
				// operand[1] will be either a var or constant
				// Can replicate an 'assign' using ADD or ADDI with the zero reg ('$0')
				mipsOperands = new MIPSOperand[3]; //[irInst.operands.length];
				Register destination = getMappedReg(irInst.operands[0].toString());
				mipsOperands[0] = destination;
				mipsOperands[1] = regs.get("zero");

				MIPSOperand source = null;
				if (irInst.operands[1] instanceof IRConstantOperand) {
					op = MIPSOp.ADDI;
					String constVal = ((IRConstantOperand) irInst.operands[1]).getValueString();
					//// TODO: Add support for "FLOAT" types, currently only checking for hex & dec
					String constType = ((constVal.toLowerCase()).indexOf('x') >= 0) 
									? "HEX" : "DEC";
					source = new Imm(constVal, constType);
				} else {
					op = MIPSOp.ADD;
					source = getMappedReg(irInst.operands[1].toString());
				}
				mipsOperands[2] = source;
				parsedInst.add(new MIPSInstruction(op, label, mipsOperands));
				break;

			case ADD:
				parsedInst.add(parseBinaryOp(MIPSOp.ADD, MIPSOp.ADDI, irInst.operands));
				break;

			case SUB:
				parsedInst.add(parseBinaryOp(MIPSOp.SUB, null, irInst.operands));
				break;

			case MULT:
				parsedInst.add(parseBinaryOp(MIPSOp.MUL, null, irInst.operands));
				break;

			case DIV:
				// TODO: Can check if last operand is a power of 2 and
				// 			replace DIV with SLL
				parsedInst.add(parseBinaryOp(MIPSOp.DIV, null, irInst.operands));
				break;
			
			case AND:
				parsedInst.add(parseBinaryOp(MIPSOp.AND, MIPSOp.ANDI, irInst.operands));
				break;
			
			case OR:
				parsedInst.add(parseBinaryOp(MIPSOp.OR, MIPSOp.ORI, irInst.operands));
				break;
			
			case BREQ:
				// ...
				// parsedInst.add(parseBranch(..., irInst.operands));
				break;
			
			case BRNEQ:
				// parsedInst.add(parseBranch(..., irInst.operands));
				break;
			
			case BRLT:
				// parsedInst.add(parseBranch(..., irInst.operands));
				break;
			
			case BRGT:
				// parsedInst.add(parseBranch(..., irInst.operands));
				break;
			
			case BRLEQ:
				// parsedInst.add(parseBranch(..., irInst.operands));
				break;
			
			case BRGEQ:
				// parsedInst.add(parseBranch(..., irInst.operands));
				break;

			case GOTO:
				// ...
				break;
			
			case RETURN:
				// ...
				break;
			
			case CALL:
				//// Load parameters for the function call ("addi $sp, $sp, stackSpace")
				int stackSpace = (irInst.operands.length) * (-4);	// Correct? (likely not...)
				parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
						regs.get("$sp"), 
						regs.get("$sp"),
						new Imm(String.valueOf(stackSpace), "DEC")));

				//// Save return address $ra in stack ("sw $ra, 0($sp)")
				parsedInst.add(new MIPSInstruction(MIPSOp.SW, null, 
						regs.get("$ra"), 
						new Addr(regs.get("$sp"))));

				//// Jump and link to the function ("jal functionName")
				//// TODO

				//// Restore return address:
				////	"lw $ra, 0($sp)"
				////	"addi $sp, $sp, 4"
				//// TODO

				//// Jump to $ra ("jr $ra")
				//// TODO

				// ...

				for (IROperand o : irInst.operands) {
					if (o instanceof IRFunctionOperand) {
						//// Replace intrinsic function calls (geti, putc, etc.) with syscalls
						switch(((IRFunctionOperand)o).getName()) {
							case "puti":
								//// TODO
								break;
							case "putc":
								//// TODO
								break;
							case "putf":
								//// TODO
								break;
							case "geti":
								//// TODO
								break;
							case "getc":
								//// TODO
								break;
							case "getf":
								//// TODO
								break;
							default:
								break;
						}
					}
					else if (o instanceof IRLabelOperand) {
						//// Would find existing label or function name in the program
					}
				}

				break;
			
			case CALLR:
				// ...
				break;
			
			case ARRAY_STORE:
				// ...
				break;
			
			case ARRAY_LOAD:
				// ...
				break;
			
			default:
				// return parsedInst;
		}



		return parsedInst;
	}

	private MIPSInstruction parseBinaryOp(MIPSOp opcode1, MIPSOp opcode2, 
                                                    IROperand[] operands) {
        MIPSOperand[] mipsOperands = new MIPSOperand[operands.length];
        MIPSOp op = opcode1;

        if (opcode2 != null) {
	        for (IROperand o : operands) {
	            if (o instanceof IRConstantOperand) {
	                op = opcode2;
	                break;
	            }
	        }
	    }

        String destination = ((IRVariableOperand) operands[0]).getName();
        mipsOperands[0] = (MIPSOperand) getMappedReg(destination);
        for (int i = 1; i < 3; i++) {
            if (operands[i] instanceof IRConstantOperand) {
                IRType type = ((IRConstantOperand) operands[i]).type;
                mipsOperands[i] = new Imm(operands[i].toString(), 
                    "DEC");     // For now: only handling integers, floats are extra credit
            } 
            else {
                mipsOperands[i] = (MIPSOperand) getMappedReg(operands[i].toString());
            }
        }

        return new MIPSInstruction(op, null, mipsOperands);
    }

/*
    private MIPSInstruction parseBranch(MIPSOp opcode1, MIPSOp opcode2, 
                                                    IROperand[] operands) {


    }
*/

/*
	public List<MIPSInstruction> parseCall(IRInstruction call) {
		List<MIPSInstruction> parsedInst = new LinkedList<>();

		//// Load parameters for the function call ("addi $sp, $sp, stackSpace")
		int stackSpace = call.operands.size() * -4;	// Correct? (likely not...)
		parsedInst.add(new MIPSInstruction(ADDI, null, 
				regs.get("$sp"), regs.get("$sp")
				new Imm(String.valueOf(stackSpace), "DEC")));

		//// Save return address $ra in stack ("sw $ra, 0($sp)")
		parsedInst.add(new MIPSInstruction(SW, null, 
			regs.get("$ra"),
			new Addr(regs.get("$ra"))));
		
	}
*/
	

	//// These instructions needed before any function call
	public List<MIPSInstruction> saveTempRegs() {
		List<MIPSInstruction> convention = new LinkedList<>();

		//// TODO

		return convention;	
	}

	//// These instructions needed after returning from a function call
	public List<MIPSInstruction> restoreTempRegs() {
		List<MIPSInstruction> convention = new LinkedList<>();

		//// TODO

		return convention;
	}



	private void initializeRegisters() {
		for (String name : tempRegNames) {
			createRealReg(name);
		}
		// createRealReg("$t0");
		// createRealReg("$t1");
		// createRealReg("$t2");
		// createRealReg("$t3");
		// createRealReg("$t4");
		// createRealReg("$t5");
		// createRealReg("$t6");
		// createRealReg("$t7");
		// createRealReg("$t8");
		// createRealReg("$t9");

		createRealReg("zero");
		createRealReg("$0");
		createRealReg("$at");

		createRealReg("$v0");
		createRealReg("$v1");

		createRealReg("$a0");
		createRealReg("$a1");
		createRealReg("$a2");
		createRealReg("$a3");

		createRealReg("$s0");
		createRealReg("$s1");
		createRealReg("$s2");
		createRealReg("$s3");
		createRealReg("$s4");
		createRealReg("$s5");
		createRealReg("$s6");
		createRealReg("$s7");

		createRealReg("$k0");
		createRealReg("$k1");

		createRealReg("$gp");
		createRealReg("$sp");
		createRealReg("$fp");
		createRealReg("$ra");
	}

	private void createRealReg(String name) {
		regs.put(name, new Register(name, false));
	}


}