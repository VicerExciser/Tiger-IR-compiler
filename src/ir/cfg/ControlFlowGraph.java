

package ir.cfg;

import ir.cfg.*;
import ir.IRUtil;
import ir.IRFunction;
import ir.IRInstruction;
import ir.operand.IROperand;
import ir.operand.IRLabelOperand;
import ir.operand.IRVariableOperand;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


/*		Control Flow Graph

	Class for building a CFG of basic blocks for a particular IRFunction instance.

	NOTE: This CFG implementation is only tailored for individual functions,
			and is not yet capable of generating a CFG for an entire program
			(should we add this functionality? is it necessary or required?)
*/
public class ControlFlowGraph {

	public static final boolean USE_MAXIMAL_BLOCKS = false;	// Else, MinBasicBlock instances will be used

	private IRFunction f;
	private Set<BasicBlockBase> blocks;
	private Set<CFGEdge> edges;
	private Set<IRInstruction> universalDefinitions;
	BasicBlockBase entryNode;

	public ControlFlowGraph(IRFunction f) {
		this.f = f;
		this.blocks = new LinkedHashSet<>();
		this.edges = new HashSet<>();
		this.universalDefinitions = new LinkedHashSet<>();
	}

	// NOTE: Currently only implemented for using MaxBasicBlocks;
	// 			for MinBasicBlocks, will simply need to generate a new block
	//			for each and every instruction, making sure to properly
	// 			create CFGEdges for each as well as updating their respective
	// 			predecessors and successors lists.
	public void build() {
		if (USE_MAXIMAL_BLOCKS) {
			// Add a fresh vertex (basic block) to the graph for each leader instruction
			IRUtil.identifyLeaders(this.f);
			generateInitialBlocks(this.f.instructions);

			BasicBlockBase curr = null;

			// Need to construct the basic blocks for each leader in this function first
			for (IRInstruction i : this.f.instructions) {
				if (i.isLeader) {
					if (curr != null) {	// Then add an edge from curr to block with leader i
						addEdge(curr, i.belongsToBlock);
					}
					curr = i.belongsToBlock;
				}

				// If i is a goto with target t then add an edge from curr to t
				if (i.opCode == IRInstruction.OpCode.GOTO) {
					IRInstruction t = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
					addEdge(curr, t.belongsToBlock);
				}
				else if (IRUtil.isConditionalBranch(i)) {
					// Append condition c to curr (i may already belong to block curr, but append is safe)
					((MaxBasicBlock)curr).appendInstruction(i);	// <-- algorithm calls for it, but truly think its unnecessary

					// Add an edge from curr to i+1
					addEdge(curr, IRUtil.getInstructionAfterThis(i).belongsToBlock);

					// Add an edge from curr to label target t
					IRInstruction t = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
					addEdge(curr, t.belongsToBlock);
				}
				else
					((MaxBasicBlock)curr).appendInstruction(i);
			}
		}
		else {
		    // MINIMAL BLOCKS
			for (IRInstruction i : this.f.instructions) {
			    // Generate BBs for every inst
				BasicBlockBase newBlock = new MinBasicBlock(this.f, i);
				////  TODO  ////
				// Will require some analysis of the instruction i here to decide
				// how edges should be created to connect to other blocks...
				////  TODO  ////
			}
			for(IRInstruction i : this.f.instructions) { // categorise instructions
				IROperand x = i.operands[0];
			    IROperand y = i.operands.length > 1 ? i.operands[1] : null;
			    IROperand z = i.operands.length > 2 ? i.operands[2] : null;
			    
			    // if i is a control flow instruction, then assign edges acordingly. otherwise, just set it to the next inst
			    
			    if(i.opCode == IRInstruction.OpCode.GOTO) { 
			        // GOTO //
			        IRInstruction temp = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
			        addEdge(i.belongsToBlock, temp.belongsToBlock);
			    }else if (IRUtil.isConditionalBranch(i)) {
			        // BRANCH //
			        addEdge(i.belongsToBlock, IRUtil.getInstructionAfterThis(this.f, i).belongsToBlock); //i+1
			        IRInstruction temp = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
			        addEdge(i.belongsToBlock, temp.belongsToBlock); //target
			    }else if(i.opCode == IRInstruction.OpCode.RETURN) {
			        // RETURN //
			        IRInstruction temp = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
                    addEdge(i.belongsToBlock, temp.belongsToBlock);
			    }//else if(i.opCode == IRInstruction.OpCode.CALL) {
			        // CALL //
			        //to my knowledge, nothing needs to be done for a call or callr because we aren't optimising between functions
			        // piazza @32
			    //}
			    else {
			        addEdge(i.belongsToBlock, IRUtil.getInstructionAfterThis(this.f, i).belongsToBlock); // i+1
			    }
			    
			}
		}

		generateReachingDefSets();
	}

	// NOTE: Also currently only implemented for using MaxBasicBlocks
	private void generateInitialBlocks(List<IRInstruction> instructions) {
		for (IRInstruction i : instructions) {
			if ((i.isLeader || i.opCode == IRInstruction.OpCode.LABEL)
					&& i.belongsToBlock == null) {
				BasicBlockBase newBlock;
				List<IRInstruction> instsToAdd = new ArrayList<>(Arrays.asList(i));

				// If inst is a label, add the very next instruction to the same new block with it
				if (i.opCode == IRInstruction.OpCode.LABEL) {
					IRInstruction nextInst = IRUtil.getInstructionAfterThis(this.f, i);
					instsToAdd.add(nextInst);
					// Account for stacked/consecutive label delcarations in source IR
					while (nextInst.opCode == IRInstruction.OpCode.LABEL) {
						nextInst = IRUtil.getInstructionAfterThis(this.f, i);
						instsToAdd.add(nextInst);
					}
				}

				newBlock = new MaxBasicBlock(this.f, instsToAdd);
				// i.belongsToBlock = newBlock;		// <-- handled by the *BasicBlock constructor

				if (this.blocks.isEmpty())
					this.entryNode = newBlock;

				this.blocks.add(newBlock);
			}
		}
	}

	private void addEdge(BasicBlockBase from, BasicBlockBase to) {
		// TODO: Assert that neither from nor to is null
		CFGEdge newEdge = new CFGEdge(from, to);
		from.successors.add(to);
		to.predecessors.add(from);
		this.edges.add(newEdge);
	}

	// NOTE: Also currently only implemented for using MaxBasicBlocks
	/*
		Step 1: Compute GEN and KILL for each basic block
		Step 2: For every basic block, make:
					OUT[B] = GEN[B]
		Step 3: While a fixed point is not found:
					IN[B] = ∪OUT[p]  where p is a predecessor of B
					OUT[B] = GEN[B] ∪ (IN[B] - KILL[B])
	 */
	public void generateReachingDefSets() {
	    if (!USE_MAXIMAL_BLOCKS) {
	        // Minimal blocks
	        
	        
	    }
		MaxBasicBlock bb;
//		Set<IRInstruction> universalDefinitions = new LinkedHashSet<>();
		universalDefinitions.clear();
		/*	LinkedHashSet methods for basic set operations:
				INTERSECTION: set1.retainAll(set2)
					--> will remove any element from set1 if it is not in set2
					( SET1 = SET1 ∩ SET2 )
				SUBTRACTION: set1.removeAll(set2)
					--> will remove any element from set1 if it is in set2
					( SET1 = SET1 - SET2 )
				UNION: set1.addAll(set2)
					--> will add any element from set2 to set1 if it does not already exist in set1
					( SET1 = SET1 ∪ SET2 )
		 */
		for (BasicBlockBase block : this.blocks) {
			bb = (MaxBasicBlock)block;
			bb.gen.clear();
			bb.kill.clear();
			bb.in.clear();
			bb.out.clear();
			bb.operandDefs.clear();
			bb.operandUses.clear();

			bb.findAllDefsAndUses();	// Will populate GEN[bb]
			bb.out.addAll(bb.gen);		// Initialize OUT[bb] = GEN[bb]
			universalDefinitions.addAll(bb.gen);
		}

		// Next, need to populate the KILL set for each block
		for (BasicBlockBase block : this.blocks) {
			bb = (MaxBasicBlock)block;
			// For each lval definition in block, find all definitions
			// throughout entire program/function that write to lval
			// regardless of whether or not they reach the block/statement
			for (String lval : bb.operandDefs.keySet()) {
				for (IRInstruction def : universalDefinitions) {
					// First operand is the lval for all definitions
					if (lval.equals(((IRVariableOperand) def.operands[0]).getName())) {
						bb.kill.add(def);
					}
				}
			}
		}

		// Initiate the iteration for computing each block's
		// IN and OUT sets for reaching definitions.
		boolean change = true;
		Set<IRInstruction> tempout = new HashSet<>();
		Set<IRInstruction> gen = new HashSet<>();
		Set<IRInstruction> inSubKill = new HashSet<>();

		// If there is a change after the iteration in any of
		// the OUT sets, then 'change' remains true
		while (change) {
			change = false;

			for (BasicBlockBase block : this.blocks) {
				bb = (MaxBasicBlock)block;
				bb.in.clear();

				// IN[bb] = ∪OUT[p] for p in the set of all predecessors of block bb
				for (BasicBlockBase p : bb.predecessors) {
					bb.in.addAll(p.out);
				}

				// OUT[bb] = GEN[bb] ∪ (IN[bb] - KILL[bb])
				tempout.clear();
				tempout.addAll(bb.out);
				bb.out.clear();

				inSubKill.clear();
				inSubKill.addAll(bb.in);
				inSubKill.removeAll(bb.kill);

				gen.clear();
				gen.addAll(bb.gen);
				gen.addAll(inSubKill);
				bb.out.addAll(gen);

				if (!(bb.out.containsAll(tempout) && tempout.containsAll(bb.out)))
					change = true;
			}
		}

	}

	public Set<CFGEdge> getEdges() {
		return this.edges;
	}

	public Set<BasicBlockBase> getBlocks() {
		return this.blocks;
	}

	public IRFunction getFunction() {
		return this.f;
	}

	public Set<IRInstruction> getUniversalDefinitions() {
		return this.universalDefinitions;
	}

	public BasicBlockBase getEntryNode() {
		return this.entryNode;
	}

}







