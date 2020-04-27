package ir.cfg;

import ir.IRFunction;
import ir.IRInstruction;
import ir.cfg.ControlFlowGraph;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
// import java.util.Comparator;

/*
	Base class for the MinBasicBlock and MaxBasicBlock subclasses
	to enable polymorphic collections.
*/
public abstract class BasicBlockBase implements Comparable<BasicBlockBase> {
	public String id;	// Block identified by parent function name + leader IR line number
	public IRFunction parent;
	public IRInstruction leader;
	public IRInstruction terminator;
	public int size;
	public int blocknum;

	public Set<IRInstruction> gen;	// Set of definitions generated in block
	public Set<IRInstruction> kill;	// Set of definitions killed in block
	public Set<IRInstruction> in;	// Set of definitions that reach beginning of block
	public Set<IRInstruction> out;	// Set of definitions that reach end of block

	// Maps to associate each variable instructio operand with a set of all
	// instructions in the block that define or use that operand, respectively
	public Map<String, Set<IRInstruction>> operandDefs;
	public Map<String, Set<IRInstruction>> operandUses;

	// The following sets are populated when the CFG is constructed
	public Set<BasicBlockBase> predecessors;
	public Set<BasicBlockBase> successors;

	// REMINDER: All blocks in dom(Bn) exist on every possible path
	// 			 from B0 to Bn
	public Set<BasicBlockBase> dom;
	public BasicBlockBase iDom;		// Immediate dominator block

	public List<List<BasicBlockBase>> pathsFromRoot;

	public static int BLOCKNUM = 0;

	public BasicBlockBase(IRFunction parent, int size,
			IRInstruction leader, IRInstruction terminator) {
		this.parent = parent;
		this.size = size;
		this.leader = leader;
		this.terminator = terminator;
		this.id = (parent != null && leader != null) 
				? (parent.name + "::" + String.valueOf(leader.irLineNumber))
				: "DEAD";

		// Should the definition sets hold instructions or blocks??
		this.gen = new HashSet<>();
		this.kill = new HashSet<>();
		this.in = new HashSet<>();
		this.out = new HashSet<>();

		this.operandDefs = new HashMap<>();
		this.operandUses = new HashMap<>();

		this.predecessors = new TreeSet<>();
		this.successors = new TreeSet<>();

		this.dom  = new LinkedHashSet<>();
		// this.dom.add(this);
		this.iDom = null;

		this.pathsFromRoot = new ArrayList<>();
		
		this.blocknum = BLOCKNUM;
		BLOCKNUM += 1;
	}

	// Constructor overload specifically for MinBasicBlock instances
	public BasicBlockBase(IRFunction parent, IRInstruction statement) {
		this(parent, 1, statement, statement);
	}

	public IRFunction getParent() {
        return parent;
    }

	@Override
	public String toString() {
		// return id;
		return "B"+this.blocknum;
	}

	@Override 
	public int compareTo(BasicBlockBase other) {
		// return this.id.compareToIgnoreCase(other.id);
		return this.blocknum - other.blocknum;
	}

	@Override 
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.parent.name.toLowerCase().hashCode();
		result = 31 * result + this.leader.irLineNumber;
		result = 31 * result + this.terminator.irLineNumber;
		result = 31 * result + this.id.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BasicBlockBase)) return false;
		if (this == obj) return true;
		BasicBlockBase bbb = (BasicBlockBase) obj;
		return this.id.equalsIgnoreCase(bbb.id)
				// && this.parent.name.equalsIgnoreCase(bbb.parent.name);
				// && this.leader.opCode == bbb.leader.opCode;
				&& this.hashCode() == bbb.hashCode();
	}

	/*
	// Abstract functions for derived classes to implement
	// for generating the local definition sets GEN and KILL
	public abstract void computeLocalDefSets();
	// and the global defintion sets IN and OUT using CFG
	public abstract void computeGlobalDefSets(ControlFlowGraph cfg);
	*/
}