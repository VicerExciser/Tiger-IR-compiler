package mips.cfg;

import mips.*;
import ir.*;
import ir.cfg.*;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.LinkedList;

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

    public MIPSBlock getBlockWithMaximalLiveRange() {
    	int maxSize = -1;
    	MIPSBlock maxBlock = null;
    	for (MIPSBlock block : this.blocks) {
    		LiveRange maxRange = block.getRangeWithMostUses();
    		if (maxRange.programPoints.size() > maxSize) {
    			maxSize = maxRange.programPoints.size();
    			maxBlock = block;
    		}
    	}
    	return maxBlock;
    }

    public void computeLiveSets() {
    	//// Using the worklist iterative algorithm
    	Deque<MIPSBlock> worklist = new LinkedList<>(this.blocks);

    	//// Initialize all LIVEINs and LIVEOUTs to empty sets
    	for (MIPSBlock block : worklist) {
    		block.liveIn.clear();
    		block.liveOut.clear();
    		// block.computeUEVar();
    		// block.computeVarKill();
    		block.computeUEAndKillSets();
    	}

    	while (worklist.peek() != null) {
    		MIPSBlock b = worklist.poll();
    		Set<String> prevLiveIn = new LinkedHashSet<>(b.liveIn);

    		//// Compute LIVEOUT(b) and LIVEIN(b)
    		b.computeLiveOut();
    		b.computeLiveIn();

    		//// If LIVEIN(b) changed at all, then add pred(b) to the worklist
    		if (!(b.liveIn.containsAll(prevLiveIn) && prevLiveIn.containsAll(b.liveIn))) {
    			for (MIPSBlock pred : b.predecessors) {
    				if (!worklist.contains(pred)) {
    					worklist.add(pred);
    				}
    			}
    		}
    	}

    	for (MIPSBlock block : this.blocks) {
    		block.computeInterference();
    	}

    }

}
