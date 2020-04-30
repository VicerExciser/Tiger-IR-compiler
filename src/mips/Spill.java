package mips;

import mips.*;
import mips.operand.*;

public class Spill {
	
	/**		IMPORTANT NOTE:
		ALL created Spill instances must be maintained in a data structure (in Selector.java, MIPSFunction.java, or MIPSBlock.java), 
		and EVERY single Spill object's spOffset attribute MUST be
		updated (incremented/decremented) ANY/EVERY time an instruction
		moves the stack pointer!!

	**/
	public int spOffset;	//// Byte offset from Stack Pointer
	public String variableName;
	

	private RegAllocator regAllocator;
	private Register sp;	//// Reference to global, singleton Stack Pointer register ($sp)

	public Spill() {
		this(0, null);
	}

	public Spill(int stackPointerOffset) { //, Register stackPointer) {
		this(stackPointerOffset, null);
	}

	public Spill(int stackPointerOffset, String irVariableName) {
		this.spOffset = stackPointerOffset;
		this.variableName = irVariableName;
		this.regAllocator = RegAllocator.getInstance();
		this.sp = regAllocator.registers.get("$sp");	//stackPointer;
	}


	public Addr getLocationOnStack() {
		// return new Addr(new Imm(String.valueOf(this.spOffset)), this.sp);

		Imm immediateStackPointerOffset = new Imm(String.valueOf(this.spOffset));
		Addr spilledValueLocationOnStack = new Addr(immediateStackPointerOffset, this.sp);
		return spilledValueLocationOnStack;
	}

	// public void incrementStackPointer()


	/**		Really just inserts an instruction such as "SW $t0, 0($sp)"
			to store the given physical register's current contents
			(the value associated with a local variable) onto the stack
			at a location relative to the stack pointer at any point in the program

		//// FIXME: Should spillRegister & recoverSpilledValue be responsible for generating
					the instructions to decrement/increment the stack pointer (respectively),
					or should this be done by the caller? 
					(currently the latter design choice... relying on the caller for $sp management)

	**/
	public MIPSInstruction spillRegister(Register sourceReg, String irVariableName) {
		this.variableName = irVariableName;
		// spillRegister(instList, sourceReg);
		return spillRegister(sourceReg);
	}

	public MIPSInstruction spillRegister(Register sourceReg) {
		//// "sw Rs, Addr"
		Addr spillLocation = getLocationOnStack();
		MIPSInstruction spillInstruction = new MIPSInstruction(
				MIPSOp.SW, 
				null, 
				sourceReg,
				spillLocation);
		spillInstruction.associatedNames.put(sourceReg.name, this.variableName);

		sourceReg.inUse = false;		//// Should Spill object be responsible for register lock/unlocking?
		return spillInstruction;
	}


	/** 	Will return an LW instruction that will load the spilled register contents
			from the spill location on the stack into a given destination register
	**/
	public MIPSInstruction recoverSpilledValue(Register destinationReg) {
		////  "lw Rd, Addr"
		Addr spillLocation = getLocationOnStack();
		MIPSInstruction unspillInstruction = new MIPSInstruction(
				MIPSOp.LW,
				null,
				destinationReg,
				spillLocation);
		unspillInstruction.associatedNames.put(destinationReg.name, this.variableName);

		destinationReg.inUse = true;	//// Should Spill object be responsible for register lock/unlocking?
		return unspillInstruction;
	}


}
