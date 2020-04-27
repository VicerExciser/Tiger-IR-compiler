package mips.cfg;

import mips.cfg.MIPSBlock;

public class MIPSCFGEdge {

	public MIPSBlock start;
	public MIPSBlock end;

	public MIPSCFGEdge(MIPSBlock start, MIPSBlock end) {
		this.start = start;
		this.end = end;
	}

	@Override 
	public String toString() {
		return this.start.toString() + "  --->  " + this.end.toString();
	}

	@Override 
	public boolean equals(Object obj) {
		return 		(obj != null)
				&&  (obj instanceof MIPSCFGEdge)
				&&  (this.start.equals(((MIPSCFGEdge)obj).start))
				&&  (this.end.equals(((MIPSCFGEdge)obj).end));
	}

	@Override 
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.start.hashCode();
		result = 31 * result + this.end.hashCode();
		return result;
	}

}