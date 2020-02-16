import ir.*;
import ir.cfg.*;
import ir.datatype.*;
import ir.operand.*;
import ir.IRInstruction.OpCode.*;

import java.io.PrintStream;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Iterator;

public class IROptimizer {

    private static String PROJECT_ROOT_DIR = "/Users/acondict/Documents/MSCS/cs4240/Tiger-IR-compiler/";
    private static String TEST_IN_FILEPATH = PROJECT_ROOT_DIR + "public_test_cases/sqrt/sqrt.ir";   //"example/example.ir";
    private static String TEST_OUT_FILEPATH = PROJECT_ROOT_DIR + "out.ir";

    ////  TODO  ////
    // Still need to throw some IRExceptions at potential failure points in
    // the basic block classes, CFG classes, and IRUtil to verify our design

    // Removes dead code from a function in the program
    // NOTE: Does not update the CFG or its Basic Blocks
    private static void sweep(IRFunction f, Set<IRInstruction> markedSet) {
        //  For each instruction i in function f, if i is not marked, then delete i
        Set<IRInstruction> removeSet = new HashSet<>();
        for (IRInstruction i : f.instructions) {
            if (!markedSet.contains(i))
                removeSet.add(i);
        }

        IRPrinter debugPrinter = new IRPrinter(System.out);
        // Dead code removal from function's instruction list
        for (IRInstruction i : removeSet) {
            // System.out.println("=== REMOVING DEAD INSTRUCTION:\n\tFunction: "
            //         + f.name + "\n\tLine: " + String.valueOf(i.irLineNumber)
            //         + "\n\tOp: " + i.opCode.toString() + "\n===\n");
            System.out.print("=== REMOVING DEAD INSTRUCTION [line "+ i.irLineNumber+"]:\t");
            debugPrinter.printInstruction(i);
            f.instructions.remove(i);
            System.out.println("Instruction removed: "+((MaxBasicBlock) i.belongsToBlock).removeInstruction(i));
        }
    }

    // private static void mark(IRFunction f, )

    private static boolean worklistContains(Deque<IRInstruction> worklist, IRInstruction inst) {
        Iterator iterator = worklist.iterator(); 
        while (iterator.hasNext()) {
            if (inst.equals(iterator.next()))
                return true;
        }
        return false;
    }

    private static void printWorklist(Deque<IRInstruction> worklist) {
        IRPrinter debugPrinter = new IRPrinter(System.out);
        Iterator iterator = worklist.iterator(); 
        System.out.println("Worklist = {");
        while (iterator.hasNext()) {
            System.out.print("\t");
            debugPrinter.printInstruction((IRInstruction)iterator.next());
        }
        System.out.println("}\n");
    }

    public static void main(String[] args) throws Exception {
        // Parse the IR file
        String infile = args.length > 0 ? args[0] : TEST_IN_FILEPATH;
        String outfile = args.length > 1 ? args[1] : TEST_OUT_FILEPATH;
        IRReader irReader = new IRReader();
        IRProgram program = irReader.parseIRFile(infile);

        List<ControlFlowGraph> allCFGs = new ArrayList<>(program.functions.size());
        IRPrinter debugPrinter = new IRPrinter(System.out);

        // Remove all useless code (an operation is useless if no operation uses its result, or if all uses of the result are themselves dead)
        // Remove all unreachable code (an operation is unreachable if no valid control-flow path contains the operation)
        for (IRFunction function : program.functions) {
            // Commence the 'Mark' routine to be followed with 'Sweep'
            // IRUtil.InstructionComparator instComparator = new IRUtil.InstructionComparator();
            // PriorityQueue<IRInstruction> worklist = new PriorityQueue<>(10, instComparator);
            Deque<IRInstruction> worklist = new LinkedList<>();
            Set<IRInstruction> marked = new HashSet<>();

            ControlFlowGraph cfg = new ControlFlowGraph(function);
            cfg.build();
            allCFGs.add(cfg);

/*
            //// NOTE: Best to analyze CFG from bottom-to-top (leaf-to-root) ////
            Set<IRInstruction> removeSet = new LinkedHashSet<>();
            // for (BasicBlockBase block : cfg.getBlocks()) {
            BasicBlockBase cfgBlocks[] = cfg.getBlocks().toArray();
            for (int idx = cfgBlocks.length; --idx >= 0;) {
                BasicBlockBase block = cfgBlocks[idx];
                MaxBasicBlock bb = (MaxBasicBlock) block;

                List<CFGEdge> edgesToNode = cfg.getEdgesToBlock(block);     // NOTE: Can do the same thing using block's predecessors set
                if (edgesToNode.isEmpty()) continue;
                else if (edgesToNode.size() == 1) {
                    // Only need to track down single chain of successors
                    boolean redefBeforeUse = false;
                    boolean defNeverUsed = true;

                    BasicBlockBase fromNode = edgesToNode.get(0).start;
                    MaxBasicBlock pp = (MaxBasicBlock) toNode;


                }
                else {
                    for (CFGEdge edge : edgesToNode) {
                        BasicBlockBase fromNode = edge.start;

                    }
                }
            }
*/

            /**     Variable operands and their defs & uses
            ================================================
            "r":    Defs = { [B0] : "assign, r, 1", 
                             [B1] : "assign, r, n", 
                             [B2] : "add, r, t1, t2"    }

                    Uses = { [B3] : "return, r"         }
            ------------------------------------------------
            "n":    Defs = { [B2] : "sub, n, n, 1",
                             [B2] : "sub, n, n, 1"      }

                    Uses = { [B0] : "brgt, if_label0, n, 1",
                             [B1] : "assign, r, n",
                             [B2] : "sub, n, n, 1",
                             [B2] : "callr, t1, fib, n",
                             [B2] : "sub, x, n, 1",
                             [B2] : "sub, n, n, 1"      }
            ------------------------------------------------
            "x":    Defs = { [B2] : "sub, x, n, 1"      }

                    Uses = { [B2] : "callr, t2, fib, x" }
            ------------------------------------------------
            "t1":   Defs = { [B2] : "callr, t1, fib, n" }

                    Uses = { [B2] : "add, r, t1, t2"    }
            ------------------------------------------------
            "t2":   Defs = { [B2] : "callr, t2, fib, x" }

                    Uses = { [B2] : "add, r, t1, t2"    } 
            ================================================
            **/
            Map<String, Set<IRInstruction>> globalDefMap = new HashMap<>();
            Map<String, Set<IRInstruction>> globalUseMap = new HashMap<>();
            for (BasicBlockBase block : cfg.getBlocks()) {
                for (String defOperand : block.operandDefs.keySet()) {
                    if (!globalDefMap.containsKey(defOperand)) 
                        globalDefMap.put(defOperand, block.operandDefs.get(defOperand));
                    else
                        globalDefMap.get(defOperand).addAll(block.operandDefs.get(defOperand));
                }
                for (String useOperand : block.operandUses.keySet()) {
                    if (!globalUseMap.containsKey(useOperand)) 
                        globalUseMap.put(useOperand, block.operandUses.get(useOperand));
                    else
                        globalUseMap.get(useOperand).addAll(block.operandUses.get(useOperand));
                }
            }

            //// FOR DEBUG ////
            System.out.println("\n<<<  GLOBAL DEF MAP >>>");
            for (String key : globalDefMap.keySet()) {
                System.out.println("Var \""+key+"\" defined by:");
                for (IRInstruction inst : globalDefMap.get(key)) {
                    System.out.print("\t[B"+inst.belongsToBlock.blocknum+"]\t");
                    debugPrinter.printInstruction(inst);
                }
            }
            System.out.println("\n<<<  GLOBAL USE MAP >>>");
            for (String key : globalUseMap.keySet()) {
                System.out.println("Var \""+key+"\" used by:");
                for (IRInstruction inst : globalUseMap.get(key)) {
                    System.out.print("\t[B"+inst.belongsToBlock.blocknum+"]\t");
                    debugPrinter.printInstruction(inst);
                }
            }
            System.out.println("\n");

            System.out.print("Conditional branch targets: {");
            // System.out.println();
            // for (IRInstruction inst : function.instructions) {
            //     if (inst.isCondBranchTarget) {
            //         System.out.print("\t");
            //         debugPrinter.printInstruction(inst);
            //     }
            // }
            for (BasicBlockBase block : cfg.getBlocks()) {
                if (((MaxBasicBlock)block).reachedByConditionalBranch())
                    System.out.print(" B"+block.blocknum+",");
            }
            System.out.println("}");

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            cfg.printAllBasicBlocks();
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
/*
            System.out.println("CFG EDGES:");
            for (CFGEdge edge : cfg.getEdges()) {
                System.out.println("\t{ "+edge.start + " --> "+edge.end+" }");
            }
            System.out.println("\n");

            BasicBlockBase exitNode = cfg.getEntryNode();
            int highestBNum = BasicBlockBase.BLOCKNUM-1;
            for (BasicBlockBase b : cfg.getBlocks()) {
                if (b.blocknum == highestBNum) {
                    exitNode = b;
                    break;
                }
            }
            for (BasicBlockBase b : cfg.getBlocks())
                cfg.printAllPaths(cfg.getEntryNode(), b);
            System.out.println("\n");
            //// FOR DEBUG ////

            cfg.generateDominatorTree();  // Now that all paths from root have been computed for each block!
*/

            // Commence the 'Mark' routine to be followed with 'Sweep'
            for (IRInstruction instruction : function.instructions) {
                // Mark all critical instructions and add to the worklist
                if (IRUtil.isCritical(instruction)) {
                    marked.add(instruction);
                    if (!worklistContains(worklist, instruction))
                        worklist.add(instruction);
                }
            }

            IRInstruction i = worklist.poll();
            while (i != null) {
                BasicBlockBase iBlock = i.belongsToBlock;
                if (iBlock == null) {
                    System.out.println("[IROptimizer-worklistMark] iBlock found null");
                    i = worklist.poll();
                    // continue;
                    break;
                }
                IRInstruction insts[] = (((MaxBasicBlock) iBlock).instructions.toArray(new IRInstruction[iBlock.size]));
                // if (IRUtil.isXUse(i)) {
                Set<IRVariableOperand> sourceOperands = IRUtil.getSourceOperands(i);
                if (!sourceOperands.isEmpty()) {
                    for (IRVariableOperand src : sourceOperands) {
                        if (src == null) continue;
                        try {
                            // Scanning all instructions that define any source variable operands used by 'i'
                            for (IRInstruction inst : globalDefMap.get(src.getName())) {
                                if (inst == null)
                                    continue;
//                                    break;
                                if (inst.belongsToBlock.equals(iBlock) || i.equals(inst))
                                    continue;
//                                    break;
                                boolean definitionReaches = true;

                                // A def reaches instruction i if it is in the IN set for block B(i)
                                // Must also ensure that the def is not killed locally within B(i) before instruction i
                                if (iBlock.in.contains(inst)) {
                                    for (int idx = 0; idx < Arrays.asList(insts).indexOf(i); idx++) {  //iBlock.size; idx++) {
                                        if (insts[idx].equals(i)) break;                        // Only scan thru block up to instruction i

                                        if (iBlock.operandDefs.containsKey(src) 
                                                && iBlock.operandDefs.get(src).contains(insts[idx]))
                                            definitionReaches = false;

                                        if (iBlock.operandUses.containsKey(src) && iBlock.operandUses.get(src).contains(insts[idx])) 
                                        {     // Could search here for unused and/or unreachable code
                                            
                                        }
                                    }

                                    /*
                                    for (IRInstruction g : iBlock.gen) {
                                        if (((IRVariableOperand) g.operands[0]).getName().equals(src.getName())) {
                                            if (g.irLineNumber < i.irLineNumber)
                                                definitionReaches = false;
                                        }
                                    }
                                    */
                                } 
                                // else definitionReaches = false;
                       


                                if (definitionReaches) {
                                    // System.out.print("$$$\tInstruction marked for sweep:\t");
                                    // debugPrinter.printInstruction(inst);

                                    if (!marked.contains(inst)) marked.add(inst);
                                    if (!worklistContains(worklist, inst)) worklist.add(inst);
                                }
                                else {
                                    // System.out.print("XXX\tDefinition determined non-reaching:\t");
                                    // debugPrinter.printInstruction(inst);
                                }
                                // System.out.print("--->\ti:\t");
                                // debugPrinter.printInstruction(i);
                                // System.out.println();
                                // printWorklist(worklist);
                            }
                        } catch (NullPointerException npe) {
//                            continue;
                            System.out.println("[IROptimizer-worklistMark] NullPointerException caught");
                            break;
                        }

                    }
                }

                for (IRInstruction g : iBlock.gen) {
                    boolean foundUseBeforeDef = false;
                    boolean foundDefBeforeDef = false;
                    boolean foundDefBeforeUseAndDef = false;
                    boolean foundUseAfterDef = false;
                    boolean foundRedefAfterDef = false;
                    boolean foundRedefAfterDefBeforeUse = false;

                    int defIdx = Arrays.asList(insts).indexOf(g);
                    for (int curIdx = 0; curIdx < iBlock.size; curIdx++) {
                        if (IRUtil.isDefinition(insts[curIdx]) 
                                && ((IRVariableOperand)insts[curIdx].operands[0]).getName().equals(((IRVariableOperand)g.operands[0]).getName())) {
                            if (curIdx < defIdx) {
                                if (foundUseBeforeDef && !foundDefBeforeDef){ 
                                    foundDefBeforeUseAndDef = true;
                                    foundDefBeforeDef = true;
                                }
                            } else if (curIdx > defIdx) {
                                foundRedefAfterDef = true;
                                if (!foundUseAfterDef)
                                    foundRedefAfterDefBeforeUse = true;
                            }
                        }
                        // ... TODO: Now check for uses ...
                    }
                }

/*
                    // If def(src) is not marked then: mark def(src), add def(src) to worklist
                    for (BasicBlockBase bb : cfg.getBlocks()) {
                        if (bb.operandDefs.containsKey(src.getName())) {    // If a definition for this source operand exists within bb
                            // (could also iterate thru the bb.gen set)
                            for (IRInstruction srcDef : bb.operandDefs.get(src.getName())) {    // Returns a set of instructions that define src

                                if (srcDef.irLineNumber > i.irLineNumber) continue;
                                boolean redefBeforeUse = false;
                                for (IRInstruction definition : cfg.getUniversalDefinitions()) {
                                    if (definition.equals(srcDef) || definition.equals(i)) continue;
                                    if (definition.irLineNumber < i.irLineNumber
                                            && definition.irLineNumber > srcDef.irLineNumber)   // NOTE: Don't think we can rely on only checking IR line #s
                                        redefBeforeUse = true;                                  // ... should check predecessors, or at least dominators
                                }
                                if (!redefBeforeUse && i.belongsToBlock.in.contains(srcDef)) {
                                    marked.add(srcDef);
                                    if (!worklist.contains(srcDef))
                                        worklist.add(srcDef);
                                } else if (!IRUtil.isCritical(srcDef)) {
                                    marked.remove(srcDef);
                                    worklist.remove(srcDef);
                                }
                            }
                        }
                    }
                }
                    // for (BasicBlockBase bb : i.belongsToBlock.predecessors) {
                    //     marked.add(bb.terminator);
                    //     if (!worklist.contains(bb.terminator))
                    //         worklist.add(bb.terminator);
                    // }
                    // for (BasicBlockBase bb : i.belongsToBlock.dom) {
                    //     marked.add(bb.terminator);
                    //     if (!worklist.contains(bb.terminator))
                    //         worklist.add(bb.terminator);
                    // }
                // }
*/

                i = worklist.poll();
            }
/*
            Set<IRInstruction> removeSet = new LinkedHashSet<>();
            for (BasicBlockBase bb : cfg.getBlocks()) 
            {
                IRInstruction insts[] = (((MaxBasicBlock) bb).instructions.toArray(new IRInstruction[bb.size]));
                int ii, jj;
                for (ii = 0; ii < insts.length; ii++) 
                {
                    boolean unused = true;
                    boolean redefined = false;
                    boolean instIsObsolete = false;
                    if (IRUtil.isDefinition(insts[ii])) 
                    {
                        // Check for killer redefinitions and unused definitions for current instruction within its own basic block
                        IRVariableOperand def = ((IRVariableOperand) insts[ii].operands[0]);
                        for (jj = ii+1; jj < insts.length; jj++) 
                        {
                            if (IRUtil.isXUse(insts[jj])) {
                                if (IRUtil.getSourceOperands(insts[jj]).contains(def))      // Found a use of our defined var
                                    unused = false;
                            }
                            
                            if (IRUtil.isDefinition(insts[jj])                          // Check if redefined before ever being used
                                    && ((IRVariableOperand) insts[jj].operands[0]).getName().equals(def.getName())) {
                                if (!unused) 
                                    redefined = true;
                            }
                            
                            instIsObsolete = (unused || redefined) && !(IRUtil.isCritical(insts[ii]));
                            if (instIsObsolete) {
                                //// FOR DEBUG ////
                                System.out.print("\n!!!  [DEAD]   Determined that the instruction:\t");
                                debugPrinter.printInstruction(insts[ii]);
                                String reason = redefined ? "REDEFINED" : unused ? "UNUSED" : "{N/A}";
                                System.out.print("!!!  is obsolete due to "+reason+" def var \""+def.getName()+"\" in:\t");
                                debugPrinter.printInstruction(insts[jj]);
                                System.out.println();
                                //// FOR DEBUG ////

                            //     marked.remove(insts[ii]);
                            //     worklist.remove(insts[ii]);
                                removeSet.add(insts[ii]);
                            }

                        }

                        

                        instIsObsolete = (unused || redefined) && !(IRUtil.isCritical(insts[ii]));

                        // Perform same checks on predecessors and successors
                        if (!instIsObsolete) {
                            //
                            for (BasicBlockBase p : bb.predecessors) {

                            }
                            for (CFGEdge e : cfg.getEdgesToBlock(bb)) {

                            }
                        }


                    }

                }

            }

            for (IRInstruction deadOp : removeSet) {
                marked.remove(deadOp);
                worklist.remove(deadOp);
            }
*/


/*
                System.out.println("======================================");
                System.out.print("\n\ni:\t");
                debugPrinter.printInstruction(i);
                if (IRUtil.isDefinition(i)) {
                    System.out.print("\tdef: "+((IRVariableOperand)i.operands[0]).getName());
                }
                if (IRUtil.isXUse(i)) {
                    System.out.print("\n\tsources: { ");
                    for (IRVariableOperand src : IRUtil.getSourceOperands(i)) {
                        System.out.print(((IRVariableOperand)src).getName());
                        System.out.print(", ");
                    }
                    System.out.println(" }");
                }
*/
                // smallBlocks(function, i, worklist, marked);
                
                // could begin at entryNode, walk predecessors until reaching target block
                // all using and defining instructions for each block is recorded in a Set
                // (value of operandDefs or operandUses HashMap)
                //}
	            			
				// For each block on path (all successors?), check
				// block.operandDefs.get(src.getName())
/*	            			
                if (IRUtil.isXUse(i)) {
                    // For each instruction j that contains a def of y or z and reaches i, mark and add to worklist
                    for (IRInstruction j : cfg.getUniversalDefinitions()) { //For j in gen(basic block)
                        if (j.equals(i)) continue;

                        if (function.instructions.indexOf(j) > function.instructions.indexOf(i)) continue;
                        if (j.irLineNumber > i.irLineNumber) continue;
*/
/*
                        System.out.print("\nj:\t");
                        debugPrinter.printInstruction(j);
                        if (IRUtil.isDefinition(j)) {
                            System.out.print("\tdef: "+((IRVariableOperand)j.operands[0]).getName());
                        }
                        if (IRUtil.isXUse(j)) {
                            System.out.print("\n\tsources: { ");
                            for (IRVariableOperand src : IRUtil.getSourceOperands(j)) {
                                System.out.print(((IRVariableOperand)src).getName());
                                System.out.print(", ");
                            }
                            System.out.println(" }");
                        }
                        System.out.println();
*/
/*
                        boolean isReachingDefinition = false;
                        for (IRVariableOperand src : IRUtil.getSourceOperands(i)) {
                            // Def'd variable is always the first operand for definitions                            
                            if (!(((IRVariableOperand) j.operands[0]).getName().equals(src.getName())))
                                continue;

                            // Check if this definition of src reaches instruction i now:
                            // A def reaches instruction i
                            //	1) if it in the IN set for the basic block B(i) containing i, and
                            //	2) the def is not killed locally within B(i) before instruction i
                            BasicBlockBase iBlock = i.belongsToBlock;

                            if (iBlock.in.contains(j)) { 
                                if (iBlock.out.contains(j))
                                    isReachingDefinition = true;
                                else
                                {
                                    // ...
                                }
                            }
*/
/*
                                if (iBlock.in.contains(j)) {
                                    // if (iBlock.leader.equals(i)) {
                                    if (iBlock.terminator.equals(i)){
                                        isReachingDefinition = true;
                                    }
                                    //else if(iBlock.out.contains(j)) {
                                     //   isReachingDefinition = true; // condition two from lecture 4, slide 4. If it is in the out set of the block, then it was not killed within the block
                                    //}
                                    else if (ControlFlowGraph.USE_MAXIMAL_BLOCKS) {
                                        // Inspect preceeding ops in the block for def killing

                                        for (IRInstruction op : ((MaxBasicBlock) iBlock).instructions) {
                                            if (op.equals(i)) {
                                                isReachingDefinition = true;
                                                break;
                                            }
                                            else if (IRUtil.isDefinition(op)
                                                    && ((IRVariableOperand) op.operands[0]).toString().equals(src.toString())) {
                                                isReachingDefinition = false;
                                                break;
                                            }
                                        }
                                    }
                                    else {	// Can skip condition 2 if analyzing instruction-level CFG
                                        isReachingDefinition = true;
                                    }
                                }
                            }
*/
/*
                        }
                        
                        if (isReachingDefinition) {
                            System.out.print("\n\n[main] Found reaching definition j:\t");
                            debugPrinter.printInstruction(j);
                            System.out.print("\t\t for instruction i:\t");
                            debugPrinter.printInstruction(i);
                            System.out.println("\n");

                            marked.add(j);
                            worklist.add(j);
                        }
                    }
                } 

                 if (IRUtil.isDefinition(i)) {
                    String def = ((IRVariableOperand) i.operands[0]).getName();

                    //
                } 
                
            }
*/
/*
            IRInstruction curOp;
            while ((curOp = worklist.poll()) != null)
            {
                // Format of curOp:  x <-- op y  OR  x <-- y op z
                IROperand y = curOp.operands[0];
                IROperand z = curOp.operands.length > 1 ? curOp.operands[1] : null;

                // Scan each instruction for operations that contain a def for y or z
                // that reaches the curOp instruction
                for (IRInstruction inst : function.instructions) {
                    // Check if inst is a definition (assign, array_load, callr, binary operations)
                    if (IRUtil.isDefinition(inst)) {
                        boolean isReachingDefinition = false;

                        // Check if inst if a def of either y or z
                        if (inst.operands[0] == y) {
                            // Check if this definition of y reaches curOp
                            // TODO
                            isReachingDefinition = true;    // based on check result
                        }
                        else if (z != null && inst.operands[0] == z) {
                            // Check if this definition of z reaches curOp
                            // TODO
                            isReachingDefinition = true;    // based on check result
                        }

                        // If inst is a reaching definition of curOp,
                        // mark it and add to worklist
                        if (isReachingDefinition) {
                            marked.add(inst);
                            worklist.add(inst);
                        }
                    }
                }
            }
*/
            sweep(function, marked);
            

            System.out.println("FUNCTION "+function.name+" ("+cfg.getBlocks().size()+" BASIC BLOCKS)\n");
            for (IRInstruction inst : function.instructions) {
                if (inst.isLeader) {
                    System.out.println("LEADER: line #"+inst.irLineNumber);
                }
            }
            System.out.println();
            // for (BasicBlockBase block : cfg.getBlocks()) {
            System.out.println("CFG EDGES:");
            for (CFGEdge edge : cfg.getEdges()) {
                // System.out.println("\t"+cfg.getBlockIndex(edge.start)+" --> "+cfg.getBlockIndex(edge.end));
                System.out.println("\t"+edge.start.blocknum+" --> "+edge.end.blocknum+"\t( "+edge.start.id+" --> "+edge.end.id+" )");
            }
            System.out.println();

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            cfg.printAllBasicBlocks();
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        }

        /*
NOTES:
• A def reaches instruction i
1) if it is in the IN set for the basic
block B(i) containing i, and
2) the def is not killed locally
within B(i) before instruction i
        */



        // Print the IR to another file

        IRPrinter filePrinter = new IRPrinter(new PrintStream(outfile));
        filePrinter.printProgram(program);

    }
    
    // @param i critical instruction pulled off worklist
    // @param f the current function holding the instruction i and the CFG
    // @param worklst the worklist
    // @param mark the marked set
    public static void smallBlocks(IRFunction f, IRInstruction i, PriorityQueue<IRInstruction> worklst, Set<IRInstruction> mark) {

        IROperand x = i.operands[0];
        IROperand y = i.operands.length > 1 ? i.operands[1] : null;
        IROperand z = i.operands.length > 2 ? i.operands[2] : null;
        
        BasicBlockBase iBlock = i.belongsToBlock; // WILL HAVE TO MORE THAN LIKELY CHANGE THIS DUE TO MINIMAL BLOCKS
        
        for(IRInstruction j : f.instructions) { // 1. for instruction j in the function,
            if(j.equals(i)) {
                //sanity repeat check. don't ask why
                break;
            }
            if(IRUtil.isDefinition(j)) { // 2. that is a def,
                IROperand jx = j.operands[0];
               
                
                if(y != null && z != null) {
                    System.out.println("i: " + String.valueOf(i.irLineNumber) + " \"" + x.toString() + " " + 
                            y.toString() + " " + z.toString() + "\". j: " + String.valueOf(j.irLineNumber) + " \"" + jx.toString()
                            + "\"");
                }else if(y != null) {
                    System.out.println("i: " + String.valueOf(i.irLineNumber) + " \"" + x.toString() + " " + 
                            y.toString() + "\". j: " + String.valueOf(j.irLineNumber) + " \"" + jx.toString()+ "\"");
                }else {
                    System.out.println("i: " + String.valueOf(i.irLineNumber) + " \"" + x.toString() + "\". j: " + 
                String.valueOf(j.irLineNumber) + " \"" + jx.toString() + "\"");
                }
                
                
                if(y != null) { // 3. of y, 
                    if(jx.toString().equals(y.toString())){
                        mark.add(j);
                        worklst.add(j);
                        
                        // 4. that reaches i,
                        /**if(iBlock.in.contains(j)) { // 5. if it is in the IN set for the BB(i)
                            mark.add(j);
                            worklst.add(j);
                        }**/
                        // TODO: condition 2
                        
                         //System.out.println("j: " +  String.valueOf(j.irLineNumber) + " has been marked. It has jx of \"" + jx.toString() + "\" and y of \"" + y.toString() + "\""); 
                    }
                }
                if(z != null) { // 3. or z,
                    if(jx.toString().equals(z.toString())){
                        mark.add(j);
                        worklst.add(j);
                        
                        // 4. that reaches i,
                       /** if(iBlock.in.contains(j)) { // 5. if it is in the IN set for the BB(i)
                            mark.add(j);
                            worklst.add(j);
                        }**/
                        // TODO: condition 2
                        
                         //System.out.println("j: " +  String.valueOf(j.irLineNumber) + " has been marked. It has jx of \"" + jx.toString() + "\" and z of \"" + z.toString() + "\""); 
                    }
                }
                
                if(IRUtil.isXUse(i)) {
                    // if i's "x" slot holds a use of a variable. Really only array assignments and returns
                    if(jx.toString().equals(x.toString())){
                        mark.add(j);
                        worklst.add(j);
                        
                        // 4. that reaches i,
                        /**if(iBlock.in.contains(j)) { // 5. if it is in the IN set for the BB(i)
                            mark.add(j);
                            worklst.add(j);
                        }**/
                        // TODO: condition 2
                        
                        // System.out.println("j: " +  String.valueOf(j.irLineNumber) + " has been marked. It has jx of \"" + jx.toString() + "\" and x of \"" + x.toString() + "\""); 
                    }
                }
               
            }
        }
        //System.out.println();
    }
    //if it is in the IN set for the basic
    //block B(i) containing i, and 


    ////  TODO  ////

}
