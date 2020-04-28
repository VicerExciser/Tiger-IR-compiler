package mips;

import mips.operand.*;

public class Spill {
	
	public int spOffset;	//// Byte offset from Stack Pointer
	private Register sp;

	public Spill(int offset, Register stackPointer) {
		this.spOffset = offset;
		this.sp = stackPointer;
	}

	public Spill(Register stackPointer) {
		this(0, stackPointer);
	}

	public Addr getAddr() {
		return new Addr(new Imm(String.valueOf(this.spOffset)), this.sp);
	}
}
