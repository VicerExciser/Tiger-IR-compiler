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

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Selector {

	public Map<String, Register> regs;
	// public Map<String, Addr> labelMap;
	// public Map<String, String> irToMipsRegMap;
	// public Map<String, Integer> assignments;

	public List<MIPSFunction> processedFunctions;
	public MIPSFunction curFunction;

	public Addr stackPointer;
	public Imm wordSize;
	public Imm negWordSize;

	private boolean SUPPORT_FLOATS = false;
	private boolean USE_SYMBOLIC = true;
	private static int tNum = 10;
	private static int aNum = 4;

	private String[] tempRegNames = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"};
	private String[] argRegNames = {"$a0", "$a1", "$a2", "$a3"};
	private String[] intrinsicFunctions = {"geti", "getf", "getc", "puti", "putf", "putc"};
	private Map<String, Imm> systemServiceCodes;

	public Selector() { 
		// labelMap = new HashMap<>();
		// irToMipsRegMap = new HashMap<>();
		// assignments = new HashMap<>();

		processedFunctions = new LinkedList<>();
		curFunction = null;

		initializeRegisters();
		initializeSystemServices();

		stackPointer = new Addr(new Imm("0"), regs.get("$sp"));
		wordSize = new Imm("4");
		negWordSize = new Imm("-4");
	}

	private String findUnusedRegName() {
		if (curFunction != null) {
			//// Find an unused temporary register to map the operand/variable name to
			for (String name : tempRegNames) {
				if (!curFunction.irToMipsRegMap.containsValue(name)) {
					return name;
				}
			}
		}
		return null;
	}

	private Register getMappedReg(String operand) {
		String associatedRegName = null;
		if (curFunction == null) {
			return null;
		}
		if (curFunction.irToMipsRegMap.containsKey(operand)) {
			associatedRegName = curFunction.irToMipsRegMap.get(operand);
			// return regs.get(associatedRegName);
		}
		else {
			/*
			//// Find an unused temporary register to map the operand/variable name to
			for (String name : tempRegNames) {
				if (!irToMipsRegMap.containsValue(name)) {
					associatedRegName = name;
					irToMipsRegMap.put(operand, associatedRegName);
					break;
				}
			}
			*/
			associatedRegName = findUnusedRegName();
			if (associatedRegName != null) {
				curFunction.irToMipsRegMap.put(operand, associatedRegName);
			}
		}

		if (associatedRegName == null) {
			if (USE_SYMBOLIC) {
				String virtualRegName = "$t" + String.valueOf(tNum) + curFunction.name; //"Virtual";
				tNum++;
				regs.put(virtualRegName, new Register(virtualRegName));
				curFunction.irToMipsRegMap.put(operand, virtualRegName);
				associatedRegName = virtualRegName;

				//// Need to initialize virtual registers before they can be used
				curFunction.instructions.add(new MIPSInstruction(MIPSOp.LI, null, regs.get(virtualRegName), new Imm("0")));
			} else {
				System.out.println("[getMappedReg] ERROR: Could not assign a temporary register to operand '"+operand+"'");
				return null;
			}
		}
		return regs.get(associatedRegName);
	}

	private Register getArgumentReg(String arg) {
		String argRegName = null;
		if (curFunction == null) {
			return null;
		}
		if (curFunction.irToMipsRegMap.containsKey(arg)) {
			argRegName = curFunction.irToMipsRegMap.get(arg);
		}
		else {
			for (String name : argRegNames) {
				if (!curFunction.irToMipsRegMap.containsValue(name)) {
					argRegName = name;
					break;
				}
			}
			if (argRegName != null) {
				curFunction.irToMipsRegMap.put(arg, argRegName);
			}
		}

		if (argRegName == null) {
			if (USE_SYMBOLIC) {
				String virtualArgName = "$a" + String.valueOf(aNum) + curFunction.name;
				aNum++;
				regs.put(virtualArgName, new Register(virtualArgName));
				curFunction.irToMipsRegMap.put(arg, virtualArgName);
				argRegName = virtualArgName;

				//// Need to initialize virtual registers before they can be used
				curFunction.instructions.add(new MIPSInstruction(MIPSOp.LI, null, regs.get(virtualArgName), new Imm("0")));
			} else {
				System.out.println("[getArgumentReg] ERROR: Could not assign a free register for argument '"+arg+"'");
				// return null;
				return getMappedReg(arg);
			}
		}
		return regs.get(argRegName);
	}

	public MIPSFunction parseFunction(IRFunction irFunction) {
		MIPSFunction mipsFunction = new MIPSFunction(irFunction.name);
		processedFunctions.add(mipsFunction);
		curFunction = mipsFunction;

		curFunction.labelMap.put(irFunction.name, new Addr(irFunction.name));

		//// Generate function name label as first instruction
        mipsFunction.instructions.add(0, new MIPSInstruction(MIPSOp.LABEL, 
        		irFunction.name, (MIPSOperand[]) null));
        //// Generate first true instruction ("move $fp, $sp")
        mipsFunction.instructions.add(1, new MIPSInstruction(MIPSOp.MOVE, null, 
        		regs.get("$fp"), regs.get("$sp")));

		if (irFunction.returnType != null) {
			mipsFunction.returnType = irFunction.returnType.toString();
		}


		if (!irFunction.parameters.isEmpty()) {
			mipsFunction.instructions.add(new MIPSInstruction(MIPSOp.COMMENT, 
					"Fetch arguments from stack", 
					(MIPSOperand) null));
			/*
				Must compute the size of params & vars for the function
				then generate instructions to reserve stack space:
					"addi $sp, $sp, -104"
				^ UPDATE: Caller should probs do this
			*/
			// int stackSpace = 0;
			List<IRVariableOperand> irParamsReversed = new LinkedList<>(irFunction.parameters);
			Collections.reverse(irParamsReversed);
			// for (IRVariableOperand irParam : irFunction.parameters) {
			for (IRVariableOperand irParam : irParamsReversed) {
				// MIPSOperand mipsParam = getArgumentReg(irParam.toString());
				MIPSOperand mipsParam = getMappedReg(irParam.toString());
				mipsFunction.parameters.add(mipsParam);

				//// Stack pointer should initially currently pointing to the last argument
				//// lw $t_, 0($sp)
				mipsFunction.instructions.add(new MIPSInstruction(MIPSOp.LW, null,
						mipsParam,
						stackPointer));

				//// addi $sp, $sp, 4
				mipsFunction.instructions.add(new MIPSInstruction(MIPSOp.ADDI, null,
						regs.get("$sp"), 
						regs.get("$sp"),
						wordSize));
				
				// stackSpace -= 4;	// Is this correct???
			}
			// mipsFunction.instructions.add(new MIPSInstruction(MIPSOp.ADDI, null,
			// 		regs.get("$sp"), regs.get("$sp"),
			// 		new Imm(String.valueOf(stackSpace))));
		}


		if (!irFunction.variables.isEmpty()) {
			for (IRVariableOperand irVar : irFunction.variables) {
				MIPSOperand mipsVar = getMappedReg(irVar.toString());
				mipsFunction.variables.add(mipsVar);
			}
		}


		for (IRInstruction irInst : irFunction.instructions) {
			List<MIPSInstruction> parsedInst = parseInstruction(irInst, irFunction.name);
			// for (MIPSInstruction mipsInst : parsedInst) {
			// 	mipsFunction.instructions.add(mipsInst);
			// }
			mipsFunction.instructions.addAll(parsedInst);
		}

		//// If not main function, append a return ("jr $ra") instruction at the end
		if (!"main".equalsIgnoreCase(mipsFunction.name)) {
			mipsFunction.instructions.add(new MIPSInstruction(MIPSOp.COMMENT, 
					"Return from subroutine " + mipsFunction.name, 
					(MIPSOperand) null));
			mipsFunction.instructions.add(new MIPSInstruction(MIPSOp.JR, null,
					regs.get("$ra")));
		}

		return mipsFunction;
	}


	public List<MIPSInstruction> parseInstruction(IRInstruction irInst, String parentName) {
		if (curFunction == null) {
			curFunction = new MIPSFunction(parentName);
			processedFunctions.add(curFunction);
		}
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
				curFunction.labelMap.put(label, new Addr(label));
				// return parsedInst;
				break;

			case ASSIGN:
				// TODO: Use MOVE instead of ADD (keep ADDI for constant operands)
				// operand[0] will be a register/variable,
				// operand[1] will be either a var or constant
			/*
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
			*/
//---------------------------------------------------------------------------------------------------
				mipsOperands = new MIPSOperand[irInst.operands.length];
				Register destination = getMappedReg(irInst.operands[0].toString());
				mipsOperands[0] = destination;
				MIPSOperand source = null;
				if (irInst.operands[1] instanceof IRConstantOperand) {
					op = MIPSOp.LI;
					String constVal = ((IRConstantOperand) irInst.operands[1]).getValueString();
					//// TODO: Add support for "FLOAT" types, currently only checking for hex & dec
					String constType = ((constVal.toLowerCase()).indexOf('x') >= 0) 
									? "HEX" : "DEC";
					source = new Imm(constVal, constType);
					curFunction.assignments.put(destination.name, ((Imm) source).getInt());
				} else if (irInst.operands[1] instanceof IRLabelOperand
						|| irInst.operands[1] instanceof IRFunctionOperand) {
					op = MIPSOp.LA;
					String addrName = irInst.operands[1].toString();
					source = new Addr(addrName);
					if (!curFunction.labelMap.containsKey(addrName)) {
						curFunction.labelMap.put(addrName, (Addr) source);
					}
				} else {
					op = MIPSOp.MOVE;
					source = getMappedReg(irInst.operands[1].toString());
				}
				mipsOperands[1] = source;
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
			
			case BREQ:	// breq, Label, Rs, Rt --> BEQ Rs, Rt, Label
/*
				mipsOperands = new MIPSOperand[3];
				op = MIPSOp.BEQ;
				// IROperand brLabel = irInst.operands[0]; // if (o instanceof IRLabelOperand)
				MIPSOperand branchTarget = null;
				String branchTargetName = new String(irInst.operands[0].toString() + "_" + parentName);
				if (labelMap.containsKey(branchTargetName)) {
					branchTarget = labelMap.get(branchTargetName);
				} else if (labelMap.containsKey(irInst.operands[0].toString())) {
					branchTarget = labelMap.get(irInst.operands[0].toString());
				} else {
					branchTarget = new Addr(branchTargetName);
					labelMap.put(branchTargetName, branchTarget);
				}

				mipsOperands[2] = branchTarget;
				IROperand brRs = irInst.operands[1];
				if (brRs instanceof IRConstantOperand) {
					if (brRs.type instanceof IRIntType) {
						mipsOperands[0] = new Imm(brRs.toString(), "DEC");
					} else {
						mipsOperands[0] = new Imm(brRs.toString(), "?");
					}
				} else {
					mipsOperands[0] = getMappedReg(brRs.toString());
				}
				IROperand brRt = irInst.operands[2];
				if (brRt instanceof IRConstantOperand) {
					if (brRt.type instanceof IRIntType) {
						mipsOperands[1] = new Imm(brRt.toString(), "DEC");
					} else {
						mipsOperands[1] = new Imm(brRt.toString(), "?");
					}
				} else {
					mipsOperands[1] = getMappedReg(brRt.toString());
				}
				parsedInst.add(new MIPSInstruction(op, label, mipsOperands));
*/
				// parsedInst.addAll(parseBranch(MIPSOp.BEQ, null, irInst.operands));
				parsedInst.add(parseBranch(MIPSOp.BEQ, irInst.operands)); //, parentName));
				break;
			
			case BRNEQ:	// brneq, Label, Rs, Rt --> BNE Rs, Rt, Label
				parsedInst.add(parseBranch(MIPSOp.BNE, irInst.operands)); //, parentName));
				break;
			
			case BRLT:	// brlt, Label, Rs, Rt --> BLT Rs, Rt, Label
				parsedInst.add(parseBranch(MIPSOp.BLT, irInst.operands)); //, parentName));
				break;
			
			case BRGT:	// brgt, Label, Rs, Rt --> BGT Rs, Rt, Label
				parsedInst.add(parseBranch(MIPSOp.BGT, irInst.operands)); //, parentName));
				break;
			
			case BRLEQ:	// brleq, Label, Rs, Rt -->  BLT Rs, Rt, Label; BEQ Rs, Rt, Label
				// beq $t0, $t1, Label 		# if $t0 = $t1, goes to Label
				// slt $t2, $t1, $t0 		# checks if $t0 > $t1
				// beq $t2, zero, Label 	# if $t0 < $t1, goes to Label
				parsedInst.add(parseBranch(MIPSOp.BLT, irInst.operands)); //, parentName));
				parsedInst.add(parseBranch(MIPSOp.BEQ, irInst.operands)); //, parentName));
				break;
			
			case BRGEQ:	// brgeq, Label, Rs, Rt --> BGE Rs, Rt, Label
				parsedInst.add(parseBranch(MIPSOp.BGE, irInst.operands)); //, parentName));
				break;

			case GOTO:
        		op = MIPSOp.J;
        		Addr jumpTarget = null;
				IRLabelOperand irTarget = (IRLabelOperand) irInst.operands[0];
				String labelName = new String(irTarget.getName() + "_" + parentName);

				if (curFunction.labelMap.containsKey(labelName)) {
					jumpTarget = curFunction.labelMap.get(labelName);
				} else if (curFunction.labelMap.containsKey(irTarget.getName())) {
					jumpTarget = curFunction.labelMap.get(irTarget.getName());
				} else {
					jumpTarget = new Addr(labelName);
					curFunction.labelMap.put(labelName, jumpTarget);
				}
				parsedInst.add(new MIPSInstruction(op, label, jumpTarget));
				break;
			
			case RETURN:
				// ...
				break;
			
			case CALL:
				String subroutineName = ((IRFunctionOperand) irInst.operands[0]).getName();

				if (Arrays.asList(intrinsicFunctions).contains(subroutineName)) {
					parsedInst.addAll(parseIntrinsicFunction(subroutineName, irInst.operands));
				} else {
					// Addr stackPointer = new Addr(new Imm("0"), regs.get("$sp"));
					// Imm wordSize = new Imm("-4");

					//// Preserve contents of $t0..$t9 on the stack
					saveTempRegs(parsedInst);

					//// Save return address $ra in stack ("sw $ra, 0($sp)")
					parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
							regs.get("$sp"), 
							regs.get("$sp"),
							negWordSize));
					parsedInst.add(new MIPSInstruction(MIPSOp.SW, null, 
							regs.get("$ra"), 
							stackPointer));

					//// Load parameters for the function call ("addi $sp, $sp, stackSpace")
					int stackSpace = 0; // = (irInst.operands.length/*-1*/) * (-4);	// Correct? (likely not...)
					// parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
					// 		regs.get("$sp"), regs.get("$sp"),
					// 		new Imm(String.valueOf(stackSpace))));
					// for (IRVariableOperand param : irInst.operands) {

					//// TODO: FIX ME!
					//// Push all arguments onto the stack
					for (int idx = 1; idx < irInst.operands.length; idx++) {
						parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
								regs.get("$sp"), 
								regs.get("$sp"),
								negWordSize));
						stackSpace -= 4;
						MIPSOperand arg = null;
						IROperand param = irInst.operands[idx];
						if (param instanceof IRVariableOperand) {
							arg = getMappedReg(param.toString());
							if (arg == null || ((Register) arg).name.contains(curFunction.name)) {
								arg = getArgumentReg(param.toString());
							}
						// } else if (param instanceof IRConstantOperand) {
						// 	arg = new Imm(param.toString());
						} else {
							// arg = new Addr(param.toString());	// prolly invalid
							System.out.println("[CALL] ERROR: Invalid function argument type");
						}
						parsedInst.add(new MIPSInstruction(MIPSOp.SW, null,
								arg, stackPointer));
					}
					

					//// Jump and link to the function ("jal functionName")
					Addr labelAddr = null;
					if (curFunction.labelMap.containsKey(subroutineName)) {
						labelAddr = curFunction.labelMap.get(subroutineName);
					} else {
						for (MIPSFunction func : processedFunctions) {
							if (subroutineName.equals(func.name)) {
								labelAddr = new Addr(func.name);
								curFunction.labelMap.put(func.name, labelAddr);
								break;
							}
						}
						if (labelAddr == null) {
							labelAddr = new Addr(subroutineName);
							curFunction.labelMap.put(subroutineName, labelAddr);
						}
					}
					parsedInst.add(new MIPSInstruction(MIPSOp.JAL, null, labelAddr));

					/* UPDATE: I believe this is done by the callee
					//// Collapse the stack:
					parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
							regs.get("$sp"), regs.get("$sp"),
							new Imm(String.valueOf(stackSpace))));
					*/

					//// Restore return address:
					////	"lw $ra, 0($sp)"
					////	"addi $sp, $sp, 4"
					parsedInst.add(new MIPSInstruction(MIPSOp.LW, null, 
							regs.get("$ra"),
							stackPointer));
					parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
							regs.get("$sp"), regs.get("$sp"),
							// new Imm(String.valueOf(stackSpace))));	// <-- need this???
							// new Imm("4")));
							wordSize));

					restoreTempRegs(parsedInst);

				}

				break;

				

				/*
				mipsOperands = new MIPSOperand[2];

				// for (IROperand o : irInst.operands) {
					// if (o instanceof IRFunctionOperand) {
				
				//// Replace intrinsic function calls (geti, putc, etc.) with syscalls
				switch(subroutineName) {

							
    // # print integer in $t0
    // ### Tiger-IR:   call, puti, t0
    // li $v0, 1   # print int
    // # li $v0, 11    # print char
    // move $a0, $t0
    // syscall

    // ### Tiger-IR:   call, putc, 10
    // li $v0, 11  # print space
    // li $a0, 10
    // syscall
							
    				//// TODO: Account for function calls with non-int/char params & more than 1 param
    				// case "putf":
					case "puti":
    					//// li $v0, 1   # print int    
						// mipsOperands = {regs.get("$v0"), new Imm("1", "DEC")};
						parsedInst.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$v0"), new Imm("1")));
						//// move $a0, intToPrint
						String intToPrint = irInst.operands[1].toString();
						mipsOperands[0] = regs.get("$a0");
						if (irInst.operands[1] instanceof IRConstantOperand) {
							mipsOperands[1] = new Imm(intToPrint);
						} else { //if (irInst.operands[1] instanceof IRVariableOperand) {
							mipsOperands[1] = getMappedReg(intToPrint);
						}
						parsedInst.add(new MIPSInstruction(MIPSOp.MOVE, null, mipsOperands));
						//// syscall
						parsedInst.add(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand) null));
						break;
					case "putc":
						//// li $v0, 11  # print char
						// mipsOperands = {regs.get("$v0"), new Imm("11", "DEC")};
						parsedInst.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$v0"), new Imm("11")));
						//// li $a0, charToPrint  # e.g., 10 to print a space character
						String charToPrint = irInst.operands[1].toString();
						mipsOperands[0] = regs.get("$a0");
						if (irInst.operands[1] instanceof IRConstantOperand) {
							mipsOperands[1] = new Imm(charToPrint);
						} else { //if (irInst.operands[1] instanceof IRVariableOperand) {
							mipsOperands[1] = getMappedReg(charToPrint);
						}
						parsedInst.add(new MIPSInstruction(MIPSOp.LI, null, mipsOperands));
						//// syscall
						parsedInst.add(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand) null));
						break;
					case "putf":
						//// TODO
						break;
					
					default:
						break;
				}
				// }
				// else if (o instanceof IRLabelOperand) {
				// 	//// Would find existing label or function name in the program
				// }
				// }

				break;
				*/
			
			case CALLR:
				subroutineName = ((IRFunctionOperand) irInst.operands[1]).getName();
				if (Arrays.asList(intrinsicFunctions).contains(subroutineName)) {
					parsedInst.addAll(parseIntrinsicFunction(subroutineName, irInst.operands));
				} else {
					//// Load parameters for the function call ("addi $sp, $sp, stackSpace")
					int stackSpace = (irInst.operands.length/*-2*/-1) * (-4);	// Correct? (likely not...)
					parsedInst.add(new MIPSInstruction(MIPSOp.ADDI, null, 
							regs.get("$sp"), 
							regs.get("$sp"),
							new Imm(String.valueOf(stackSpace))));

					//// Save return address $ra in stack ("sw $ra, 0($sp)")
					parsedInst.add(new MIPSInstruction(MIPSOp.SW, null, 
							regs.get("$ra"), 
							new Addr(regs.get("$sp"))));


					//// Jump and link to the function ("jal functionName")
					//// TODO: Check if an intrinsic function; do this ^ if not

					//// Restore return address:
					////	"lw $ra, 0($sp)"
					////	"addi $sp, $sp, 4"
					//// TODO

					//// Jump to $ra ("jr $ra")
					//// TODO

				}

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
                curFunction.assignments.put(destination, ((Imm) mipsOperands[i]).getInt());
            } 
            else {
                mipsOperands[i] = (MIPSOperand) getMappedReg(operands[i].toString());
            }
        }

        return new MIPSInstruction(op, null, mipsOperands);
    }

    // private List<MIPSInstruction> parseBranch(MIPSOp opcode1, MIPSOp opcode2, 
    private MIPSInstruction parseBranch(MIPSOp op, IROperand[] operands) { //, 
                                                    // String parentFuncName) {
        // List<MIPSInstruction> parsedBranch = new LinkedList<>();
        MIPSOperand[] mipsOperands = new MIPSOperand[operands.length];
        Addr branchTarget = null;
        String labelName = operands[0].toString();
        String branchTargetName = new String(labelName + "_" + curFunction.name); //parentFuncName);

        if (curFunction.labelMap.containsKey(branchTargetName)) {
            branchTarget = curFunction.labelMap.get(branchTargetName);
        } else if (curFunction.labelMap.containsKey(labelName)) {
            branchTarget = curFunction.labelMap.get(labelName);
        } else {
            branchTarget = new Addr(branchTargetName);
            curFunction.labelMap.put(branchTargetName, branchTarget);
        }
        mipsOperands[2] = branchTarget;

        for (int i = 1; i <= 2; i++) {
            if (operands[i] instanceof IRConstantOperand) {
                if (((IRConstantOperand) operands[i]).type instanceof IRIntType) {
                    mipsOperands[i-1] = new Imm(operands[i].toString()); //, "DEC");
                } else {
                    mipsOperands[i-1] = new Imm(operands[i].toString(), "?");
                }
            } else {
                mipsOperands[i-1] = getMappedReg(operands[i].toString());
            }
        }

        return new MIPSInstruction(op, null, mipsOperands);
    }

    /*
	    # print integer in $t0
	    ### Tiger-IR:   call, puti, t0
	    li $v0, 1   # print int
	    # li $v0, 11    # print char
	    move $a0, $t0
	    syscall

	    ### Tiger-IR:   call, putc, 10
	    li $v0, 11  # print space
	    li $a0, 10
	    syscall
	*/
	public List<MIPSInstruction> parseIntrinsicFunction(String name, IROperand[] operands) {
		List<MIPSInstruction> parsedFunc = new LinkedList<>();
		MIPSOperand[] mipsOperands;
		Imm serviceCode = null;
		MIPSOperand destination = null;
		switch(name) {
			    				//// TODO: Account for function calls with non-int/char params & more than 1 param
			case "puti":		//// print_int
				parsedFunc.add(new MIPSInstruction(MIPSOp.COMMENT, "print_int", 
						(MIPSOperand) null));
				//// li $v0, 1     
				serviceCode = systemServiceCodes.get("print_int");
				parsedFunc.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$v0"), serviceCode));

				//// move $a0, intToPrint
				String intToPrint = operands[1].toString();
				MIPSOp opcode = null;
				mipsOperands = new MIPSOperand[2];
				mipsOperands[0] = regs.get("$a0");
				
				if (operands[1] instanceof IRConstantOperand) {
					mipsOperands[1] = new Imm(intToPrint);
					opcode = MIPSOp.LI;
				} else { //if (irInst.operands[1] instanceof IRVariableOperand) {
					mipsOperands[1] = getMappedReg(intToPrint);
					opcode = MIPSOp.MOVE;
				}
				parsedFunc.add(new MIPSInstruction(opcode, null, mipsOperands));

				//// syscall
				parsedFunc.add(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand) null));
				break;
			
			case "putc":		//// print_char
				parsedFunc.add(new MIPSInstruction(MIPSOp.COMMENT, "print_char", 
						(MIPSOperand) null));
				//// li $v0, 11 
				serviceCode = systemServiceCodes.get("print_char");
				parsedFunc.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$v0"), serviceCode));

				//// li $a0, charToPrint  # e.g., 10 to print a space character
				String charToPrint = operands[1].toString();
				opcode = null;
				mipsOperands = new MIPSOperand[2];
				mipsOperands[0] = regs.get("$a0");

				if (operands[1] instanceof IRConstantOperand) {
					mipsOperands[1] = new Imm(charToPrint);
					opcode = MIPSOp.LI;
				} else { //if (operands[1] instanceof IRVariableOperand) {
					mipsOperands[1] = getMappedReg(charToPrint);
					opcode = MIPSOp.MOVE;
				}
				parsedFunc.add(new MIPSInstruction(opcode, null, mipsOperands));

				//// syscall
				parsedFunc.add(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand) null));
				break;

			case "putf":
				//// TODO
				if (!SUPPORT_FLOATS) {
					System.out.println("[parseIntrinsicFunction] ERROR: call to 'putf' not supported!");
				}
				break;

			case "geti":		//// read_int
				parsedFunc.add(new MIPSInstruction(MIPSOp.COMMENT, "read_int", 
						(MIPSOperand) null));
				//// li $v0, 5
				serviceCode = systemServiceCodes.get("read_int");
				parsedFunc.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$v0"), serviceCode));

				//// syscall (will store int read from keyboard into $v0)
				parsedFunc.add(new MIPSInstruction(MIPSOp.SYSCALL, null, (MIPSOperand) null));

				// //// sw $v0, intValue
				// MIPSOperand intValue = 

				//// move destination, $v0  
				// MIPSOperand destination;
				if (operands[0] instanceof IRVariableOperand) {
					destination = getMappedReg(operands[0].toString());
				} else {	//// Should never be the case
					destination = new Addr(operands[0].toString());
				}
				parsedFunc.add(new MIPSInstruction(MIPSOp.MOVE, null, destination, regs.get("$v0")));
				break;

			case "getc":		//// read_string (for a single char)
				parsedFunc.add(new MIPSInstruction(MIPSOp.COMMENT, "read_char", 
						(MIPSOperand) null));
				mipsOperands = new MIPSOperand[2];
				serviceCode = systemServiceCodes.get("read_string");
				//// li $v0, 8
				parsedFunc.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$v0"), serviceCode));

				//// la $a0, bufferAddr
				mipsOperands[0] = regs.get("$a0");
				destination = getMappedReg(((IRVariableOperand) operands[0]).getName());
				mipsOperands[1] = new Addr((Register) destination);
				parsedFunc.add(new MIPSInstruction(MIPSOp.LA, null, mipsOperands));

				//// li $a1, lengthOfOne
				parsedFunc.add(new MIPSInstruction(MIPSOp.LI, null, regs.get("$a1"), new Imm("1")));
				break;

			case "getf":
				//// TODO
				if (!SUPPORT_FLOATS) {
					System.out.println("[parseIntrinsicFunction] ERROR: call to 'getf' not supported!");
				}
				break;
			
			default:
				break;
		}

		return parsedFunc;
	}

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
	public void saveTempRegs(List<MIPSInstruction> convention) {
		// List<MIPSInstruction> convention = new LinkedList<>();
		convention.add(new MIPSInstruction(MIPSOp.COMMENT, 
				"Saving temporary regs", 
				(MIPSOperand) null));

		for (String temp : tempRegNames) {
			//// addi $sp, $sp, -4
			convention.add(new MIPSInstruction(MIPSOp.ADDI, null, 
					regs.get("$sp"), 
					regs.get("$sp"),
					negWordSize));

			//// sw $t_, 0($sp)
			convention.add(new MIPSInstruction(MIPSOp.SW, null,
					regs.get(temp), 
					stackPointer));
		}

		// return convention;	
	}

	//// These instructions needed after returning from a function call
	public void /*List<MIPSInstruction>*/ restoreTempRegs(List<MIPSInstruction> convention) {
		// List<MIPSInstruction> convention = new LinkedList<>();
		List<String> reversedRegNames = Arrays.asList(tempRegNames);      
      	Collections.reverse(reversedRegNames);

      	convention.add(new MIPSInstruction(MIPSOp.COMMENT, 
				"Restoring temporary regs", 
				(MIPSOperand) null));

		for (String temp : reversedRegNames) {
			//// lw $t_, 0($sp)
			convention.add(new MIPSInstruction(MIPSOp.LW, null,
					regs.get(temp),
					stackPointer));

			//// addi $sp, $sp, 4
			convention.add(new MIPSInstruction(MIPSOp.ADDI, null, 
					regs.get("$sp"), 
					regs.get("$sp"),
					wordSize));
		}

		// return convention;
	}



	private void initializeRegisters() {
		regs = new HashMap<>();

		for (String name : tempRegNames) {
			////  $t0..$t9
			createRealReg(name);
		}

		for (String name : argRegNames) {
			////  $a0..$a3
			createRealReg(name);
		}

		createRealReg("zero");
		createRealReg("$0");
		// createRealReg("$at");

		createRealReg("$v0");
		createRealReg("$v1");

		createRealReg("$s0");
		createRealReg("$s1");
		createRealReg("$s2");
		createRealReg("$s3");
		createRealReg("$s4");
		createRealReg("$s5");
		createRealReg("$s6");
		createRealReg("$s7");

		// createRealReg("$k0");
		// createRealReg("$k1");

		createRealReg("$gp");
		createRealReg("$sp");
		createRealReg("$fp");
		createRealReg("$ra");
	}

	private void createRealReg(String name) {
		regs.put(name, new Register(name, false));
	}

	private void initializeSystemServices() {
		systemServiceCodes = new HashMap<>();
		systemServiceCodes.put("print_int", new Imm("1"));
		systemServiceCodes.put("print_float", new Imm("2"));
		systemServiceCodes.put("print_double", new Imm("3"));
		systemServiceCodes.put("print_string", new Imm("4"));
		systemServiceCodes.put("read_int", new Imm("5"));
		systemServiceCodes.put("read_float", new Imm("6"));
		systemServiceCodes.put("read_double", new Imm("7"));
		systemServiceCodes.put("read_string", new Imm("8"));
		systemServiceCodes.put("sbrk", new Imm("9"));
		systemServiceCodes.put("print_char", new Imm("11"));
	}

}