package ir.cfg;

import ir.IRFunction;
import ir.IRInstruction;


/*		Minimal Basic Block 

	Each MinBasicBlock instance represents a single instruction/statement
	(following an instruction-level CFG design, for simplicity),
	the instruction's definition sets (IN, OUT, GEN, KILL),
	predecessors, and successors.
*/
public class MinBasicBlock extends BasicBlockBase {

	public IRInstruction s;		// A single statement

	public MinBasicBlock(IRFunction parent, IRInstruction statement) {
		super(parent, statement);
		this.s = statement;
		statement.belongsToBlock = (BasicBlockBase)this;
	}

	// Populate this block's GEN and KILL local definition sets
	public void computeLocalDefSets() {
		// GEN[S] = set of definitions in S ("generated" by S)

		//// TODO ////

		// KILL[S] = set of definitions that may be overwritten by S 
		//	(e.g., all definitions in program that write to S's lval,
		//	whether or not the reach S)

		//// TODO ////

	}

	// Populate this block's IN and OUT global definition sets using CFG
	public void computeGlobalDefSets(ControlFlowGraph cfg) {
		// IN[S] = set of definitions that reach the entry point of S

		//// TODO ////

		// OUT[S] = set of definitions in S as well as definitions from IN[S]
		// 	that go beyond S (are not "killed" by S)

		//// TODO ////

	}

}


/*
	Data flow equations (invariants) for the definition sets:

		OUT[S] = GEN[S] U (IN[S] - KILL[S])
		 IN[S] = Union of OUT[p] sets for all p in predecessors


	Kill: a definition d1 of a variable v is killed between p1 and p2
			if in every path from p1 to p2 there is another definition of v.

	Reach: a definition di reaches a point pj if there exists any path di --> pj,
			and di is not killed along the path.
*/


			