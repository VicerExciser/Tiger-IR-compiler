package ir.cfg;

import ir.IRFunction;
import ir.IRInstruction;
import ir.cfg.ControlFlowGraph;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

	public Set<IRInstruction> gen;
	public Set<IRInstruction> kill;
	public Set<IRInstruction> in;
	public Set<IRInstruction> out;

	public Set<BasicBlockBase> predecessors;
	public Set<BasicBlockBase> successors;

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

		this.predecessors = new LinkedHashSet<>();
		this.successors = new LinkedHashSet<>();
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
		return id;
	}

	@Override 
	public int compareTo(BasicBlockBase other) {
		return this.id.compareToIgnoreCase(other.id);
	}

	@Override 
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.parent.name.toLowerCase().hashCode();
		result = 31 * result + this.leader.irLineNumber;
		result = 31 * result + this.terminator.irLineNumber;
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

	// Abstract functions for derived classes to implement
	// for generating the local definition sets GEN and KILL
	public abstract void computeLocalDefSets();
	// and the global defintion sets IN and OUT using CFG
	public abstract void computeGlobalDefSets(ControlFlowGraph cfg);
	
}