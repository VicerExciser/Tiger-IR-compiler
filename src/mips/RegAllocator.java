package mips;

import mips.*;
import mips.cfg.*;
import mips.operand.*;

import java.util.Map;
import java.util.HashMap;

public class RegAllocator {		//// Singleton

	private static RegAllocator singletonInstance = null;

	private boolean USE_VIRTUAL = true;
	private boolean PRINTS_ENABLED = true;
	private boolean STRONG_ARM = true;
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

	public String[] tempRegNames = {"$t0", "$t1", "$t2", "$t3", "$t4", 
									"$t5", "$t6", "$t7", "$t8", "$t9"};
	public String[] argRegNames = {"$a0", "$a1", "$a2", "$a3"};


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
        this.virtualRegs = new HashMap<>();
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


	//// FIXME: Make damn sure that Registers are getting unlocked!
	public String findUnusedRegName() {
		for (String name : tempRegNames) {
		// for (int i = 0; i < tempRegNames.length; i++) {		//// Will iteratively check for free reg in order
		// 	String name = "$t" + String.valueOf(i);
			if (!this.registers.get(name).inUse) {
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
		} else {
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


	//// FIX-ME
	public Register getMappedRegForBlock(String operand, MIPSBlock curBlock) {
		String associatedRegName = null;
		Register tReg = null;
		if (curBlock == null) {
			return tReg;
		}
		if (curBlock.irToMipsRegMap.containsKey(operand)) {
			associatedRegName = curBlock.irToMipsRegMap.get(operand);
		} else {
			//// Find an unused temporary register to map the operand/variable name to
			associatedRegName = findUnusedRegName(curBlock.parentFunction);
			// associatedRegName = findUnusedRegName();
			if (associatedRegName != null) {
				curBlock.parentFunction.irToMipsRegMap.put(operand, associatedRegName);
				curBlock.registerLocalVariable(operand, associatedRegName);
			}
		}

		/*  Possibly promising start here:
		if (associatedRegName == null) {
			if (curBlock.parentFunction.irToMipsRegMap.containsKey(operand)) {
				associatedRegName = curBlock.parentFunction.irToMipsRegMap.get(operand);
				if (associatedRegName != null) {

				}
			}
		}
		*/

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
					curBlock.irToMipsRegMap.put(operand, associatedRegName);
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
