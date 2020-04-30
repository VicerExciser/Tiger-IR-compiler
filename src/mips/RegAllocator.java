/**		
	Regarding register allocation using live intervals:

	● 	Given the live intervals for all the variables in the program, we can
		allocate registers using a simple greedy algorithm.

	● 	Idea: Track which registers are free at each point.

	● 	When a live interval begins, give that variable a free register.

	● 	When a live interval ends, the register is once again free.
**/

package mips;

import mips.*;
import mips.cfg.*;
import mips.operand.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RegAllocator {		//// Singleton

	private static RegAllocator singletonInstance = null;

	private boolean RESERVE_SPILL_REGS = false; //true;
	private boolean USE_VIRTUAL = true;
	private boolean PRINTS_ENABLED = true;
	private boolean STRONG_ARM = false; //true;
	private static int tNum = 10;
	private static int reallocCtr = 0;
	
	public enum Mode {
		NAIVE,
		INTRABLOCK;

		private static final int size = Mode.values().length;
	}

	public Mode mode;
	public Map<String, Register> registers;
	public Map<String, Register> virtualRegs;
	public Map<String, Spill> spilledRegMap;	//// Maps variable/operand names to the 
												//// corresponding value's location on the stack

	public String[] tempRegNames = {"$t0", "$t1", "$t2", "$t3", "$t4", 
									"$t5", "$t6", "$t7", "$t8", "$t9"};
	public String[] argRegNames = {"$a0", "$a1", "$a2", "$a3"};
	public String[] regsReservedForSpills = {"$t7", "$t8", "$t9"};


	private RegAllocator(int allocationMode) {
		if (allocationMode >= Mode.size || allocationMode < 0) {
			if (PRINTS_ENABLED) {
	            System.out.println("[RegAllocator] INVALID MODE SETTING: "
	            		+ String.valueOf(allocationMode)
	            		+ "\nDEFAULTING ALLOCATION MODE TO '" 
	            		+ Mode.NAIVE + "'\n");
	        }
            this.mode = Mode.NAIVE;
        } else {
            this.mode = Mode.values()[allocationMode];
            if (PRINTS_ENABLED) {
	            System.out.println("[RegAllocator] Allocation Mode set to: '"
	            		+ this.mode.name() + "'\n");
	        }
        }

        initializeRegisters();

        if (mode != Mode.NAIVE && RESERVE_SPILL_REGS) {
        	for (String reservedRegName : regsReservedForSpills) {
        		registers.get(reservedRegName).reservedForSpill = true;
        	}
        }
        
        this.virtualRegs = new HashMap<>();
        this.spilledRegMap = new HashMap<>();
	}

	public static RegAllocator getInstance(int allocationMode) {
		if (singletonInstance == null) {
			singletonInstance = new RegAllocator(allocationMode);
		}
		return singletonInstance;
	}

	public static RegAllocator getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new RegAllocator(-1);
		}
		return singletonInstance;
	}


	public String findUnusedRegName(MIPSFunction curFunction) {
		String unused = findUnusedRegName();
		if (unused != null && !registers.get(unused).reservedForSpill) {
			return unused;
		}

		//// Reserve 3 registers at all times for Spills' sake
		if (getNumCurrentlyFreeRegs() < 3) {
			return null;
		}

		if (curFunction != null) {
			//// Find an unused temporary register to map the operand/variable name to
			for (String name : tempRegNames) {
				if (registers.get(name).reservedForSpill) {
					continue;
				}

				if (!curFunction.irToMipsRegMap.containsValue(name)) {
					return name;
				}
			}
		}
		return null;
	}

	private  String findUnusedSpillRegName() {
		for (String name : regsReservedForSpills) {
			if (!registers.get(name).inUse) {
				return name;
			}
		}
		return null;
	}


	//// FIXME: Make damn sure that Registers are getting unlocked!
	public String findUnusedRegName() {
		//// Reserve 3 registers at all times for Spills' sake
		if (RESERVE_SPILL_REGS && getNumCurrentlyFreeRegs() < 3) {
			return null;
		}

		for (String name : tempRegNames) {
		// for (int i = 0; i < tempRegNames.length; i++) {		//// Will iteratively check for free reg in order
		// 	String name = "$t" + String.valueOf(i);
			if (!registers.get(name).inUse && !registers.get(name).reservedForSpill) {
				return name;
			}
		}
		return null;
	}


	public void freeRegister(String regName) {
		freeRegister(registers.get(regName));
	}

	public void freeRegister(Register reg) {
		reg.inUse = false;

		//// TODO: Should this ensure that all irToMipsReg mappings for this register are deleted?

	}

	public void lockRegister(String regName) {
		lockRegister(registers.get(regName));
	}

	public void lockRegister(Register reg) {
		reg.inUse = true;
	}


	//// Locks/reserves & returns one of the 10 physical temporary registers (if any are available)
	public Register getUnusedRegNaive() {
		if (this.mode != Mode.NAIVE) {
			return null;
		}
		String regName = findUnusedRegName();
		if (regName == null) {
			System.out.println("\n  !!! [getUnusedRegNaive]  ERROR  ERROR  ERROR  !!!\n");
			return null;
		}
		Register availableReg = registers.get(regName);
		lockRegister(availableReg);
		return availableReg;
	}


	public Register getMappedRegForFunction(String operand, MIPSFunction curFunction) {
		String associatedRegName = null;
		Register tReg = null;
		if (curFunction == null) {
			return tReg;
		}
		if (curFunction.irToMipsRegMap.containsKey(operand)) {
			associatedRegName = curFunction.irToMipsRegMap.get(operand);
		} 
		else {
			//// Find an unused temporary register to map the operand/variable name to
			associatedRegName = findUnusedRegName(curFunction);
			// associatedRegName = findUnusedRegName();
			if (associatedRegName != null) {
				curFunction.irToMipsRegMap.put(operand, associatedRegName);
			}
		}

		if (associatedRegName == null) {
			if (USE_VIRTUAL) {
				String virtualRegName = "$t" + String.valueOf(tNum) + curFunction.name; //"Virtual";
				tNum++;
				Register virtualReg = new Register(virtualRegName);
				registers.put(virtualRegName, virtualReg);
				virtualRegs.put(virtualRegName, virtualReg);

				curFunction.irToMipsRegMap.put(operand, virtualRegName);
				associatedRegName = virtualRegName;

				//// Need to initialize virtual registers before they can be used (?)
				// curFunction.addInstructionToCurrentBlock(new MIPSInstruction(MIPSOp.LI, null, virtualReg, new Imm("0")));
			} else {
				if (STRONG_ARM) {
					associatedRegName = "$t" + String.valueOf(reallocCtr);
					reallocCtr = (reallocCtr + 1) % tempRegNames.length;
					curFunction.irToMipsRegMap.put(operand, associatedRegName);
					curFunction.getCurrentBlock().irToMipsRegMap.put(operand, associatedRegName);
				} else {
					System.out.println("[getMappedRegForFunction] ERROR: Could not assign a temporary register to operand '"+operand+"'");
					return tReg;
				}
			}
		}
		tReg = registers.get(associatedRegName);
		tReg.inUse = true;
		return tReg;
	}


	//// FIXME
	public Register getMappedRegForBlock(String operand, MIPSBlock curBlock) {
		String associatedRegName = null;
		Register tReg = null;
		if (curBlock == null || curBlock.parentFunction == null) {
			return tReg;
		}
		if (curBlock.irToMipsRegMap.containsKey(operand)) {
			associatedRegName = curBlock.irToMipsRegMap.get(operand);
		} 
		//// TODO: Check this...
		else if (curBlock.parentFunction.irToMipsRegMap.containsKey(operand)) {
			associatedRegName = curBlock.parentFunction.irToMipsRegMap.get(operand);
		}
		//// TODO ^
		else {
			//// Find an unused temporary register to map the operand/variable name to
			associatedRegName = findUnusedRegName(curBlock.parentFunction);
			// associatedRegName = findUnusedRegName();
			if (associatedRegName != null) {
				curBlock.parentFunction.irToMipsRegMap.put(operand, associatedRegName);
				// curBlock.irToMipsRegMap.put(operand, associatedRegName);
				curBlock.registerLocalVariable(operand, associatedRegName);
			}
		}

		if (associatedRegName == null) {
			if (USE_VIRTUAL) {
				// String virtualRegName = "$t" + String.valueOf(tNum) + curBlock.id; //"Virtual";
				// String virtualRegName = "$t" + String.valueOf(tNum) + curBlock.id.replace("_",""); //"Virtual";
				String virtualRegName = "$t" + String.valueOf(tNum) + curBlock.parentFunction.name;
				tNum++;
				Register virtualReg = new Register(virtualRegName);
				registers.put(virtualRegName, virtualReg);
				virtualRegs.put(virtualRegName, virtualReg);

				curBlock.parentFunction.irToMipsRegMap.put(operand, virtualRegName);
				curBlock.registerLocalVariable(operand, virtualRegName);

				associatedRegName = virtualRegName;

				//// Need to initialize virtual registers before they can be used (?)
				// curFunction.addInstructionToCurrentBlock(new MIPSInstruction(MIPSOp.LI, null, virtualReg, new Imm("0")));
			} else {
				if (STRONG_ARM) {
					associatedRegName = "$t" + String.valueOf(reallocCtr);
					reallocCtr = (reallocCtr + 1) % tempRegNames.length;
					curBlock.parentFunction.irToMipsRegMap.put(operand, associatedRegName);
					curBlock.registerLocalVariable(operand, associatedRegName);
				} else {
					System.out.println("[getMappedRegForBlock] ERROR: Could not assign a temporary register to operand '"+operand+"'");
					return tReg;
				}

				//// TODO: Spill candidate registers to the stack to free them up
			}
		}
		tReg = registers.get(associatedRegName);
		tReg.inUse = true;
		return tReg;
	}


	//// CALL THIS EVERY TIME THE STACK GROWS / $sp IS DECREMENTED (i.e., "addi $sp, $sp, -16")
	//// stackPointerChange value MUST be a multiple of 4 as it represents a number of bytes!
	public void incrementAllSpillOffsets(int stackPointerChange) {
		int delta = Math.abs(stackPointerChange);
		for (Spill location : spilledRegMap.values()) {
			location.spOffset += delta;
		} 
	}

	//// CALL THIS EVERY TIME THE STACK SHRINKS / $sp IS INCREMENTED (i.e., "addi $sp, $sp, 16")
	public void decrementAllSpillOffsets(int stackPointerChange) {
		int delta = Math.abs(stackPointerChange);
		for (Spill location : spilledRegMap.values()) {
			location.spOffset -= delta;
		} 
	}


	/** With the swapRegister() method, the RegAllocator only needs to determine
		when a register should be evicted or replaced with a physical register 
		(if the current reg is virtual) for an instruction, fetch a replacement
		register, then call swapRegister
	**/
	public void swapRegister(MIPSInstruction inst, String oldRegName, String newRegName) {
		swapRegister(inst, registers.get(oldRegName), registers.get(newRegName));		
	}

	public void swapRegister(MIPSInstruction inst, Register oldReg, Register newReg) {
		// if (inst == null || inst.operands == null || inst.operands.isEmpty()) {
		// 	return;
		// }

		List<MIPSOperand> currentOperands = new ArrayList<>(inst.operands);
		for (MIPSOperand o : currentOperands) {
			if (o instanceof Register && ((Register)o).equals(oldReg)) {
				inst.operands.set(inst.operands.indexOf(o), newReg);
				freeRegister(oldReg);
				lockRegister(newReg);
				String assocName = inst.associatedNames.get(oldReg.name);
				if (!inst.associatedNames.containsKey(newReg.name)) {
					inst.associatedNames.put(newReg.name, assocName);
				} else {
					inst.associatedNames.replace(newReg.name, assocName);
				}
				inst.associatedNames.remove(oldReg.name, assocName);
				break;
			} else if (o instanceof Addr && ((Addr) o).mode == Addr.Mode.REGISTER) {
				if (((Addr) o).register.name.equals(oldReg.name)) {
					((Addr) inst.operands.get(inst.operands.indexOf(o))).register = newReg;
				}
			}

		}
	}

	// private void spillVictimReg(MIPSBlock curBlock, int instIdx, Register victim, String varName) {
	private void spillVictimReg(MIPSInstruction inst, Register victim, String varName) {
		MIPSBlock curBlock = inst.parentBlock;

		//// If a stack location (Spill) already exists for the variable, retrieve it; else, create one
		Spill stackLocation = spilledRegMap.get(varName);

		MIPSInstruction spillComment = new MIPSInstruction(MIPSOp.COMMENT,
				"Spilling victim reg '" + victim.name + "' w/ value '" 
				+ varName + "' to stack",
				(MIPSOperand) null);
		curBlock.insertInstructionAtIdx(spillComment, 
				curBlock.getInstructionIdx(inst));

		if (stackLocation == null) {
			//// Allocate a word on the stack where the variable's value will persist
			MIPSInstruction growStackInst = new MIPSInstruction(MIPSOp.ADDI, null,
					registers.get("$sp"),
					registers.get("$sp"),
					new Imm("-4"));
			curBlock.insertInstructionAtIdx(growStackInst, 
					curBlock.getInstructionIdx(inst));

			//// Adjust all Spill stack pointer offsets accordingly after growing the stack
			incrementAllSpillOffsets(4);

			//// Create & map a new Spill object (representing a memory location on the stack) 
			//// at 0 offset from the current $sp
			stackLocation = new Spill(0, varName);
			spilledRegMap.put(varName, stackLocation);
		}

		//// Spill result value to its mapped location on the stack (by inserting a SW instruction)
		MIPSInstruction spillInst = stackLocation.spillRegister(victim);

		//// Insert the spill instructions into the block at (before)
		//// the current instruction's position
		curBlock.insertInstructionAtIdx(spillInst, curBlock.getInstructionIdx(inst));
	}

	private Register findVictimRegFor(MIPSInstruction inst, Register oldReg, 
														String oldRegVarName) {
		Register victim = null;
		String victimRegName = findUnusedRegName();
		String victimVarName = null; 
		MIPSBlock curBlock = inst.parentBlock;

		//// If it just so happens that a non-virtual register is available, take it
		if (victimRegName != null) {
			victim = registers.get(victimRegName);
			if (victim.isVirtual || !victim.name.startsWith("$t")) {
				victim = null;
			} else {
				return victim;
			}
		}

		curBlock.computeInterference();
/*
		for (String[] interferingVarPair : curBlock.interferingVars) {
			// ...
		}
*/
		//// Search through any earlier instructions in the block looking for dead vars
		int instIdxInBlock = curBlock.getInstructionIdx(inst);
		MIPSInstruction[] blockInstructions = curBlock.getInstructionsArray();
		for (int i = 0; i < instIdxInBlock; i++) {
			if (blockInstructions[i].associatedNames == null) {
				continue;
			}
			if (victim != null) {
				break;
			}

			// for (String val : blockInstructions[i].associatedNames.values()) {
			for (Register srcReg : blockInstructions[i].getReads()) {
				if (!srcReg.name.startsWith("$t")) {
					continue;
				}
				String srcVarName = blockInstructions[i].associatedNames.get(srcReg.name);
				if (srcVarName == null) {
					System.out.println("\n /// [findVictimRegFor] Instruction is missing an associatedNames mapping for read reg '"+srcReg.name+"':\n"+blockInstructions[i].toString()+"\n ///\n");
					continue;
				}
				if (!curBlock.isVariableUsedPastPoint(srcVarName, blockInstructions[i])) {
					victim = srcReg;
					victimVarName = srcVarName;
					System.out.println("\n+++ [findVictimRegFor] VICTIM REG FOUND (source):  "+victimVarName+"  +++\n");
					break;
				}
			}
			if (victim == null) {
				Register destReg = blockInstructions[i].getWrite();
				if (destReg != null && destReg.name.startsWith("$t")) {
					String destVarName = blockInstructions[i].associatedNames.get(destReg.name);
					if (!curBlock.isVariableUsedPastPoint(destVarName, blockInstructions[i])) {
						victim = destReg;
						victimVarName = destVarName;
						System.out.println("\n+++ [findVictimRegFor] VICTIM REG FOUND (dest):  "+victimVarName+"  +++\n");
					}
				}
			}
		}

		//// No suitable replacement found in earlier block instructions...
		//// Perhaps just take one and treat it in a naive manner?
		if (victim == null) {
			System.out.println("\n--- [findVictimRegFor] STILL NO VICTIM REG FOUND FOR:  "+oldRegVarName+"  ---\n");

			//// FIXME: Temporary implementation!!
			victim = registers.get("$v1");
			return victim;
		}

		//// TODO: Implement search logic for finding a victim candidate (USE LIVE SETS)
		//// TODO: Reverse lookup for victimVarName
		// for (String name : tempRegNames)
		// for (String regName : curBlock.irToMipsRegMap.values())
		// 	if (isVariableUsedPastPoint())
//// TODO


		//// Need to spill the victim reg to the stack
		spillVictimReg(inst, victim, victimVarName);

		return victim;
	}

	//// (1) Identify any MIPSInstruction that is still assigned a virtual register
	//// (2) Perform intra-block live range analysis to determine a candidate replacement reg
	//// (3) Swap the virtual for the discovered physical; spill previous reg holder's value to the stack
	//// (4) Update all mappings for the effected registers
	public void virtualRegReplacementPass(MIPSCFG cfg) {
		for (MIPSBlock block : cfg.blocks) {
			List<MIPSInstruction> currentInstructions = new ArrayList<>(block.instructions);
			for (MIPSInstruction inst : currentInstructions) {
				if (inst.usesVirtualReg) {
					List<Register> currentVirtualRegs = inst.virtualRegOperands;
					List<String> removedRegs = new ArrayList<>();
					// for (Register virtualReg : inst.virtualRegOperands) {
					for (Register virtualReg : currentVirtualRegs) {
						if (virtualReg.isVirtual && virtualRegs.containsKey(virtualReg.name) && !removedRegs.contains(virtualReg.name)) {
							System.out.println("\n{ virtualRegReplacementPass } REPLACING '"+virtualReg.name+"' IN BLOCK '"+block.toString()+"' FOR INSTRUCTION "+inst.toString()+"\n");
							//// Reg needs to be swapped
							String varName = inst.associatedNames.get(virtualReg.name);
							Register replacement = findVictimRegFor(inst, virtualReg, varName);
							swapRegister(inst, virtualReg, replacement);

							inst.virtualRegOperands.remove(virtualReg.name);
							removedRegs.add(virtualReg.name);
							inst.usesVirtualReg = false;

							//// Make the same swap for any further instructions that
							//// may still reference the replaced virtual register
							MIPSInstruction[] iArr = block.getInstructionsArray();
							for (int i = block.getInstructionIdx(inst); i < block.size(); i++) {
								for (MIPSOperand o : iArr[i].operands) {
									if (o instanceof Register) {
										if (((Register) o).name.equals(virtualReg.name)) {
											swapRegister(iArr[i], virtualReg, replacement);
										}
									} else if (o instanceof Addr && ((Addr) o).mode == Addr.Mode.REGISTER) {
										if (((Addr) o).register.name.equals(virtualReg.name)) {
											swapRegister(iArr[i], virtualReg, replacement);
										}
									}
								}

								// for (Register used : iArr[i].getReads()) {
								// 	if (used.equals(virtualReg)) {
								// 		swapRegister(iArr[i], virtualReg, replacement);
								// 	}
								// }
								// Register written = iArr[i].getWrite();
								// if (written != null && written.equals(virtualReg)) {
								// 	swapRegister(iArr[i], virtualReg, replacement);
								// }
								
							}

						}
					}
				}
			/*
				if (inst.hasVariableOperands()) {
					for (MIPSOperand op : inst.operands) {
						if (op instanceof Register) {
							Register reg = (Register) op;
							if (reg.isVirtual || virtualRegs.containsKey(reg.name)) {
								//// reg needs to be swapped
								String varName = inst.associatedNames.get(reg.name);

							}
						}
					}
				}
			*/
			}
		}
	}


	public List<String> getCurrentlyFreeRegNames() {
		List<String> freeNames = new ArrayList<>();
		for (String name : this.registers.keySet()) {
			if (!this.registers.get(name).inUse) {
				freeNames.add(name);
			}
		}
		return freeNames;
	}

	public int getNumCurrentlyFreeRegs() {
		int freeCount = 0;
		for (Register reg : this.registers.values()) {
			if (!reg.inUse) {
				freeCount++;
			}
		}
		return freeCount;
	}


	public void printAllRegisters() {
		printPhysicalRegisters();
		printVirtualRegisters();
	}
	public void printPhysicalRegisters() {
		System.out.println("\n PHYSICAL REGISTERS:");
		for (String key : this.registers.keySet()) {
			Register reg = this.registers.get(key);
			if (reg != null && !reg.isVirtual) {
				System.out.println("\t"+reg.toString());
				// System.out.println("\t"+key);
			}
		}
		System.out.println();
	}
	public void printVirtualRegisters() {
		System.out.println("\n VIRTUAL REGISTERS:");
		for (String key : this.virtualRegs.keySet()) {
			Register reg = this.virtualRegs.get(key);
			if (reg != null && reg.isVirtual) {
				// System.out.println("\t"+reg.toString());
				System.out.println("\t"+key);
			}
		}
		System.out.println();
	}


	private void initializeRegisters() {
		this.registers = new HashMap<>();

		for (String name : tempRegNames) {
			////  $t0..$t9
			createRealReg(name);
		}

		for (String name : argRegNames) {
			////  $a0..$a3
			createRealReg(name);
		}

		createRealReg("zero");
		createRealReg("$zero");
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
		this.registers.put(name, new Register(name, false));
	}



}
