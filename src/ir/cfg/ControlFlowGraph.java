package ir.cfg;

import ir.cfg.*;
import ir.IRUtil;
import ir.IRFunction;
import ir.IRInstruction;
import ir.operand.IROperand;
import ir.operand.IRLabelOperand;

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

	public static final boolean USE_MAXIMAL_BLOCKS = true;	// Else, MinBasicBlock instances will be used

	private IRFunction f;
	private Set<BasicBlockBase> blocks;
	private Set<CFGEdge> edges;
	BasicBlockBase entryNode;

	public ControlFlowGraph(IRFunction f) {
		this.f = f;
		this.blocks = new LinkedHashSet<>();
		this.edges = new HashSet<>();
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
			for (IRInstruction i : this.f.instructions) {
				BasicBlockBase newBlock = new MinBasicBlock(this.f, i);
				////  TODO  ////
				// Will require some analysis of the instruction i here to decide
				// how edges should be created to connect to other blocks...
				////  TODO  ////
			}
		}


    }

    // NOTE: Also currently only implemented for using MaxBasicBlocks
    private void generateInitialBlocks(List<IRInstruction> instructions) {
    	for (IRInstruction i : instructions) {
    		if ((i.isLeader || i.opCode == IRInstruction.OpCode.LABEL) 
    				&& i.belongsToBlock == null) {
    			BasicBlockBase newBlock;
    			List<IRInstruction> instsToAdd = new ArrayList<>(Arrays.asList(i));

    			// If inst is a label, add the very next instruction to the same new block with it
    			if (i.opCode == IRInstruction.OpCode.LABEL) 
    				instsToAdd.add(IRUtil.getInstructionAfterThis(this.f, i));

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

    public Set<CFGEdge> getEdges() {
    	return this.edges;
    }

    public Set<BasicBlockBase> getBlocks() {
    	return this.blocks;
    }

    public IRFunction getFunction() {
    	return this.f;
    }

    public BasicBlockBase getEntryNode() {
    	return this.entryNode;
    }
	
}







