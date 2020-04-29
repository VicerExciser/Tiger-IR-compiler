package mips.cfg;

import mips.*;
import ir.*;
import ir.cfg.*;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;

public class MIPSCFG {
	
	public String parentName;
	public Set<MIPSBlock> blocks;
	public Set<MIPSCFGEdge> edges;
	public MIPSBlock entryNode;
	public Map<BasicBlockBase, MIPSBlock> irBlockMap;

	public MIPSCFG(String name) {
		this.parentName = name;
		this.blocks = new LinkedHashSet<>();
		this.edges = new HashSet<>();
		this.entryNode = null;
		this.irBlockMap = new HashMap<>();
	}

	public void registerBlock(MIPSBlock block) {
		if (this.blocks.isEmpty() || this.entryNode == null) {
			this.entryNode = block;
		}
		this.blocks.add(block);
		this.irBlockMap.put(block.associatedIRBlock, block);
	}

	public MIPSBlock getBlockWithID(String blockID) {
        if (blockID == null) return null;
        for (MIPSBlock block : this.blocks) {
            if (blockID.equalsIgnoreCase(block.id)) {
                return block;
            }
        }
        return null;
    }

    public MIPSBlock getBlockWithAssociatedIRBlock(BasicBlockBase associated) {
    	/*
    	if (associated == null) {
    		System.out.println("[MIPSBlock::getBlockWithAssociatedIRBlock] ERROR: BasicBlockBase parameter is null!");
    		return null;
    	} else if (!this.irBlockMap.keySet().contains(associated)) {
    		System.out.println("[MIPSBlock::getBlockWithAssociatedIRBlock] ERROR: BasicBlockBase parameter not found as valid key in MIPSCFG.irBlockMap!\n -->\t(produced by block ID: "+associated.id+")");
    		return null;
    	}
    	return this.irBlockMap.get(associated);
    	*/
    	for (MIPSBlock block : this.blocks) {
    		if (block.associatedIRBlock.equals(associated)) {
    			return block;
    		}
    	}
		System.out.println("[MIPSBlock::getBlockWithAssociatedIRBlock] ERROR: BasicBlockBase parameter not found in MIPSCFG.irBlockMap!\n -->\t(produced by block ID: "+associated.id+")");
    	return null;
    }

    public void computeLiveSets() {
    	//// Using the worklist iterative algorithm

    }

    private void computeLiveinLiveout() {
    	/*	Data-flow problems are expressed as simultaneous equations:
				
				LIVEOUT(b) = ∪s∈succ(b) LIVEIN(s)		<--- use IMEDIATE (touching) successors
				LIVEIN(b) = UEVAR(b) ∪ (LIVEOUT(b) - VARKILL(b))
			
			where
				
				UEVAR(b) is the set of names used in block b before being defined
						in b (Upwards Exposed Variables)

				VARKILL(b) is the set of variables assigned in b

			Solve the equations using a fixed-point iterative scheme.
    	*/

    }


}
