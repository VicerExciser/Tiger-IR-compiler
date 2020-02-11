package ir.cfg;

import ir.*;
import ir.operand.IRVariableOperand;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

/*		Maximal Basic Block

	This class is designed to represent groupings of instructions as maximal basic blocks
	belonging to a particular parent function. Attributes include
	the block's definition sets (IN, OUT, GEN, KILL),
	predecessors, and successors.
*/
public class MaxBasicBlock extends BasicBlockBase {

	public Set<IRInstruction> instructions;
	
	public IRInstruction topLabel;	// The first instruction iff it's of type LABEL, else null

	public MaxBasicBlock(IRFunction parent, List<IRInstruction> instructions) {
		super(parent, instructions.size(), instructions.get(0), 
				instructions.get(instructions.size()-1));

		// If leader instruction for this block is a label (i.e., a branch target),
		// set the topLabel instance variable to the label for future references
		// and update the leader instruction instance variable so that it points
		// to the first non-label instruction of this block.
		this.topLabel = null;
		int i = 0;
		while (i < instructions.size() && instructions.get(i).opCode == IRInstruction.OpCode.LABEL) {
			this.topLabel = instructions.get(i);
			this.leader = instructions.get(i++);
		}

		// We want to keep the instructions ordered, yet ensure no duplicates can exist:
		this.instructions = new LinkedHashSet<>(instructions);
		for (IRInstruction inst : this.instructions)
			inst.belongsToBlock = (BasicBlockBase)this;
	}

	// Return true if the first statement in this block is a LABEL, else false
	public boolean beginsWithLabel() {
		return this.topLabel != null;
	}
	
	// Return true if successful, else false if current terminator is a control 
	// flow statement or if it already exists in this block ('.add()' will fail)
	public boolean appendInstruction(IRInstruction inst) {
		if (IRUtil.isControlFlow(this.terminator) || !this.instructions.add(inst))
			return false;
		this.size = this.instructions.size();
		this.terminator = inst;
		inst.belongsToBlock = (BasicBlockBase)this;
		return true;
	}

	public boolean removeInstruction(IRInstruction inst) {
		boolean retVal = false;
		List<IRInstruction> instList = new ArrayList<>(this.instructions);
		IRInstruction prev = null;
		IRInstruction curr = null;
		IRInstruction next = null;
		int i = 0;
		while (i < instList.size()) 
		{
			if (!this.instructions.contains(instList.get(i))) {
				i++;
				continue;
			}

			curr = instList.get(i);

			next = (i+1 < instList.size() && this.instructions.contains(instList.get(i+1)))
					? instList.get(i+1) : null;
			// 	next = instList.get(i+1);
			// else
			// 	next = null;

			if (curr.equals(inst)) {
				if (this.leader.equals(inst)) {
					this.leader = next;
					if (next != null) next.isLeader = true;
				}
				if (this.terminator.equals(inst))
					this.terminator = prev;

				if (inst.belongsToBlock.equals(this))
					inst.belongsToBlock = null;

				retVal = this.instructions.remove(inst);
				this.size = this.instructions.size();
				break;
			}

			prev = curr;
			i++;
		}
		return retVal;
	}

	// Populates this block's GEN set, as well as the mappings for
	// each defined or used variable to its defining or using instruction, respectively
	public void findAllDefsAndUses() {
		for (IRInstruction inst : this.instructions) {
			int i;
			IRVariableOperand instDef = null;

			if (IRUtil.isDefinition(inst)) {
				this.gen.add(inst);
				// First operand is the LHS target for all definition instructions
				instDef = (IRVariableOperand) inst.operands[0];
			}

			if (instDef != null) {
				String key = instDef.getName();
				if (!this.operandDefs.containsKey(key))
					this.operandDefs.put(key, new LinkedHashSet<IRInstruction>());
				this.operandDefs.get(key).add(inst);
			}

			for (IRVariableOperand use : IRUtil.getSourceOperands(inst)) {
				String key = use.getName();
				if (!this.operandUses.containsKey(key))
					this.operandUses.put(key, new LinkedHashSet<IRInstruction>());
				this.operandUses.get(key).add(inst);
			}
		}
	}

//	public void computeGenSet() {
//		for (IRInstruction op : this.instructions) {
//			if (IRUtil.isDefinition(op)) {
//				this.gen.add(op);
//			}
//		}
//	}
//
//	public void computeKillSet(Set<IRInstruction> universalDefSet) {
//
//	}

	// Populate this block's GEN and KILL local definition sets
//	public void computeLocalDefSets() {
		// GEN[S] = set of definitions in S ("generated" by S)

		//// TODO ////

//		for (BasicBlockBase bb : )


		// KILL[S] = set of definitions that may be overwritten by S 
		//	(e.g., all definitions in program that write to S's lval,
		//	whether or not the reach S)

		//// TODO ////

//	}

	// Populate this block's IN and OUT global definition sets using CFG
//	public void computeGlobalDefSets(ControlFlowGraph cfg) {
		// IN[S] = set of definitions that reach the entry point of S

		
		//// TODO ////


		// OUT[S] = set of definitions in S as well as definitions from IN[S]
		// 	that go beyond S (are not "killed" by S)

		//// TODO ////

//	}

	
}


/*
	Data flow equations (invariants) for the definition sets:

		OUT[S] = GEN[S] ∪ (IN[S] - KILL[S])
		 IN[S] = Union of OUT[p] sets for all p in predecessors


	Kill: a definition d1 of a variable v is killed between p1 and p2
			if in every path from p1 to p2 there is another definition of v.

	Reach: a definition di reaches a point pj if there exists any path di --> pj,
			and di is not killed along the path.
*/


			