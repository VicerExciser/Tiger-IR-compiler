package mips;

import mips.operand.Register;

import java.util.Map;
import java.util.HashMap;

public class RegAllocator {

	private boolean PRINTS_ENABLED = true;
	
	public enum Mode {
		NAIVE,
		INTRABLOCK;

		private static final int size = Mode.values().length;
	}

	public Mode mode;
	public Map<String, Register> registers;

	public String[] tempRegNames = {"$t0", "$t1", "$t2", "$t3", "$t4", 
									"$t5", "$t6", "$t7", "$t8", "$t9"};
	public String[] argRegNames = {"$a0", "$a1", "$a2", "$a3"};


	public RegAllocator(int allocationMode) {
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
