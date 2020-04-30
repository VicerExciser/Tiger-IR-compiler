package mips;

// import mips.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;


/**
	• Live: variable at current program point will be used in the
			future before it is overwritten

	• Dead: variable at current program point will be overwritten
			before it is used

	• Live Range: set of program points where a variable is live
			(need not be a contiguous interval in general)
**/
public class LiveRange {

	public /*MIPSOperand*/ String varName;
	// public boolean alive; // needsReg;

	public MIPSInstruction startInst;
	public MIPSInstruction endInst;
	// public List<MIPSInstruction> programPoints;
	// public List<Integer> ppIndices;
	public Map<Integer, MIPSInstruction> programPoints;
	// List<Integer> sortedPointIndexList;

	public int startIdx;	//// Index of first block instruction where var is live
							//// (relative to ONLY the associated MIPSBlock's instructions Set)
	public int endIdx;		//// Index of the associated MIPSBlock instruction that 
							//// marks the end of the variable's live range interval
							//// (thus, the var no longer needs to hold its assigned Register)



	public LiveRange(String varName) {
		this.varName = varName;
		this.programPoints = new HashMap<>();
	}

	// public LiveRange(String varName, )


	public List<Integer> getSortedInstructionIndexList() {
		List<Integer> sortedKeys = new ArrayList<>(programPoints.keySet());

		Collections.sort(sortedKeys, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return a.compareTo(b);
			}
		});

		/*
		//// FOR DEBUG
		System.out.println(" | LiveRange("+varName+") |");
		for (Integer idx : sortedKeys) {
			System.out.println("  blockInstructions["+String.valueOf(idx)+"] = "
					+ programPoints.get(idx));
		}
		//// FOR DEBUG
		*/

		return sortedKeys;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" | LiveRange(");
		builder.append(varName);
		builder.append(") |");
		for (Integer idx : getSortedInstructionIndexList()) {
			builder.append("  blockInstructions[");
			builder.append(String.valueOf(idx));
			builder.append("] = ");
			builder.append(programPoints.get(idx));
		}
		builder.append("\n");
		return builder.toString();
	}

}
