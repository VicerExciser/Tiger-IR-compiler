package ir.cfg;

import ir.cfg.BasicBlockBase;

/*
	Note that each instance for this class is a directed edge 
	that connects two basic block vertices in a control flow graph.
*/
public class CFGEdge {
	BasicBlockBase start;
	BasicBlockBase end;
	
	public CFGEdge(BasicBlockBase start, BasicBlockBase end) {
		this.start = start;
		this.end = end;
	}

	@Override 
	public boolean equals(Object obj) {
		return 		(obj != null)
				&&  (obj instanceof CFGEdge)
				&&  (this.start.equals(((CFGEdge)obj).start))
				&&  (this.end.equals(((CFGEdge)obj).end));
	}
}
