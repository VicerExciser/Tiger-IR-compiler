package mips.operand;

import mips.*;
import mips.operand.*;

public class MIPSArray extends MIPSOperand {
	
	public String name;
	public int size;
	public Addr start;
	public Addr end;
	public MIPSFunction parent;

	//// TODO: Add type attribute?

	public MIPSArray(String name, int size,
			Addr start, Addr end,
			MIPSFunction parent) {
		this.name = name;
		this.size = size;
		this.start = start;
		this.end = end;
		this.parent = parent;
	}

	public void printArrayInfo() {
		System.out.println("Array '"+name+"' ("+parent.name+"):");
		System.out.println("\tSize: "+String.valueOf(size));
		System.out.println("\tStart: "+start.toString());
		System.out.println("\tEnd: "+end.toString());
	}

	@Override
    public String toString() {
        return name;
    }

}
