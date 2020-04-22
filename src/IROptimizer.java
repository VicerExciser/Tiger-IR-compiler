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
    private static String TEST_OUT_FILEPATH = PROJECT_ROOT_DIR + "optimized.ir";

    public static boolean VERBOSE_REACHING_DEFS = true; //false;

    ////  TODO  ////
    // Still need to throw some IRExceptions at potential failure points in
    // the basic block classes, CFG classes, and IRUtil to verify our design

    // Removes dead code from a function in the program
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
            System.out.print("=== REMOVING DEAD INSTRUCTION ===   [line "+ i.irLineNumber+"]:\t");
            debugPrinter.printInstruction(i);
            f.instructions.remove(i);
            ((MaxBasicBlock) i.belongsToBlock).removeInstruction(i);
        }
    }

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

        // List<ControlFlowGraph> allCFGs = new ArrayList<>(program.functions.size());
        IRPrinter debugPrinter = new IRPrinter(System.out);

        // Remove all useless code (an operation is useless if no operation uses its result, or if all uses of the result are themselves dead)
        // Remove all unreachable code (an operation is unreachable if no valid control-flow path contains the operation)
        for (IRFunction function : program.functions) {
            System.out.println("\n ==--==--====--==--====--==--====--==--====--==--==");
            System.out.println("==--====--==  FUNCTION { \""+function.name+"\" }  ==--====--==");
            System.out.println(" ==--==--====--==--====--==--====--==--====--==--==\n");
            // Commence the 'Mark' routine to be followed with 'Sweep'
            // IRUtil.InstructionComparator instComparator = new IRUtil.InstructionComparator();
            // PriorityQueue<IRInstruction> worklist = new PriorityQueue<>(10, instComparator);
            Deque<IRInstruction> worklist = new LinkedList<>();
            Set<IRInstruction> marked = new HashSet<>();

            ControlFlowGraph cfg = new ControlFlowGraph(function);
            cfg.build();
            // allCFGs.add(cfg);

            System.out.println("("+cfg.getBlocks().size()+" BASIC BLOCKS)");

            /**     Variable operands and their defs & uses (for example.ir)
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
            System.out.println("\n\n<<<  GLOBAL DEF MAP >>>");
            for (String key : globalDefMap.keySet()) {
                System.out.println("Var \""+key+"\" defined by:");
                for (IRInstruction inst : globalDefMap.get(key)) {
                    System.out.print("\t["+inst.belongsToBlock+"] "/*"+inst.irLineNumber+".*/+"\t");
                    debugPrinter.printInstruction(inst);
                }
            }
            System.out.println("\n\n<<<  GLOBAL USE MAP >>>");
            for (String key : globalUseMap.keySet()) {
                System.out.println("Var \""+key+"\" used by:");
                for (IRInstruction inst : globalUseMap.get(key)) {
                    System.out.print("\t["+inst.belongsToBlock+"] "/*+inst.irLineNumber+".*/+"\t");
                    debugPrinter.printInstruction(inst);
                }
            }
            // System.out.println("\n");

            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            cfg.printAllBasicBlocks();
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

            computeAllReachingDefinitions(function);

            // Commence the 'Mark' routine to be followed with 'Sweep'
            for (IRInstruction instruction : function.instructions) {

                if (instruction.reachingDefinitions == null)
                    instruction.reachingDefinitions = new ArrayList<>();

                // Mark all critical instructions and add to the worklist
                if (IRUtil.isCritical(instruction)) {
                    marked.add(instruction);
                    if (!worklistContains(worklist, instruction))
                        worklist.add(instruction);
                }
            }

            IRInstruction i = worklist.poll();
            while (i != null) {
                // System.out.print("i:\t");
                // debugPrinter.printInstruction(i);
                if (VERBOSE_REACHING_DEFS) {
                    System.out.print(">  i ("+i.belongsToBlock+", line "+i.irLineNumber+"):\t");
                    debugPrinter.printInstruction(i);
                    // System.out.println("\ti.sourceOperands = "+IRUtil.getSourceOperands(i)); //+"\n");
                    System.out.println();
                }

                MaxBasicBlock iBlock = (MaxBasicBlock) i.belongsToBlock;
                if (iBlock == null) {
                //     System.out.println("[IROptimizer::Mark] iBlock found null");
                    i = worklist.poll();
                    break;
                }

                // (i has form "x <-- op y" or "x <-- y op z")
                // if (IRUtil.isXUse(i)) {
                if (!IRUtil.getSourceOperands(i).isEmpty()) {
                    // For each instruction j that contains a def of y or z that reaches i...
                    Set<IRInstruction> checkSet = new HashSet<>(iBlock.in);
                    checkSet.addAll(iBlock.instructions);
                    for (IRInstruction j : checkSet) {
                        if (IRUtil.isDefinition(j)) {
                            if (j.equals(i)) continue;      // Should exclude self...right?

                            // The first operand of a defining instruction is the LHS value to be written
                            IRVariableOperand lhs = (IRVariableOperand) j.operands[0];
                            boolean isReachingDefinition = false;

                            for (IRVariableOperand rhs : IRUtil.getSourceOperands(i)) {
                                if (lhs.getName().equals(rhs.getName())) {
                                    //// FOR DEBUG
                                    if (VERBOSE_REACHING_DEFS) {
                                        System.out.print(">>  j ("+j.belongsToBlock+", line "+j.irLineNumber+"):\t");
                                        debugPrinter.printInstruction(j);
                                    }
                                    // System.out.println();
                                    //// FOR DEBUG

                                    // if (isReachingDef(i, j, function)) {
                                    if (isReachingDef(i, j, function, cfg)) {
                                    // if (isReachingDef2(i, j, function, cfg)) {
                                        // if (i.reachingDefinitions == null)
                                        //     i.reachingDefinitions = new ArrayList<>();
                                        // if (i.reachingDefinitions != null)
                                            i.reachingDefinitions.add(j);
                                        // else
                                        //     System.out.println("ERROR: i.reachingDefinitions is null!");

                                        if (VERBOSE_REACHING_DEFS)
                                            System.out.println("[IROptimizer::Mark] Instruction 'i' is reached by def 'j'\n");
                                        isReachingDefinition = true;
                                        break;
                                    } 
                                    else if (VERBOSE_REACHING_DEFS)
                                        System.out.println("[IROptimizer::Mark] Instruction 'i' is NOT reached by def 'j'\n");
                                }
                                
                            }

                            if (isReachingDefinition) {
                                if (!marked.contains(j)) {
                                    marked.add(j);
                                
                                    if (!worklistContains(worklist, j)) {
                                        worklist.add(j);
                                        if (VERBOSE_REACHING_DEFS)
                                            printWorklist(worklist);
                                    }
                                }
                            }
                        }
                    }
                }
                if (worklist.isEmpty()) break;
                i = worklist.poll();
                if (VERBOSE_REACHING_DEFS)
                    System.out.println("-----------------------------------------------------------------------");
            }

            for (IRInstruction inst : function.instructions) {
                System.out.print("\nReaching definitions for instruction [line "+inst.irLineNumber+"]: ");
                debugPrinter.printInstruction(inst);
                for (IRInstruction def : inst.reachingDefinitions) {
                    System.out.print("\t[line "+def.irLineNumber+"]:\t");
                    debugPrinter.printInstruction(def);
                }
                // System.out.println();
            }
            System.out.println("\n");


            sweep(function, marked);
            
            System.out.println();
        }

        // Print the IR to another file
        IRPrinter filePrinter = new IRPrinter(new PrintStream(outfile));
        filePrinter.printProgram(program);
    }

    private static void computeAllReachingDefinitions(IRFunction f) {
        for (IRInstruction i : f.instructions) {
            Set<IRVariableOperand> uses = IRUtil.getSourceOperands(i);
            if (uses.isEmpty()) continue;
            BasicBlockBase iB = i.belongsToBlock;
            
            for (List<BasicBlockBase> pathFromRoot : iB.pathsFromRoot) {
                List<IRInstruction> path = new ArrayList<>();   // All instructions on path
                for (BasicBlockBase bb : pathFromRoot) {
                    if (bb.equals(iB)) {
                        boolean ignoreInstructions = false;
                        for (IRInstruction instruction : ((MaxBasicBlock) bb).instructions) {
                            // Add only instructions up to and including i -- none after i
                            if (!ignoreInstructions) {
                                path.add(instruction);
                                if (instruction.equals(i))  
                                    ignoreInstructions = true;
                            }
                        }
                    }
                    else 
                        path.addAll(((MaxBasicBlock) bb).instructions); 
                }

                int iIdx = path.indexOf(i);
                if (iIdx < 0) continue;         // Something went wrong and j was not found

                // boolean foundUseBeforeRedef = false;
                boolean jKilled = false;

                // Walk every instruction from root to i (exclusive) on this path
                for (int idx = 0; idx <= iIdx; idx++) {

                    /* Q: Does it even matter if there are any uses of defVar between j and a redefinition??? */
                    if (!jKilled) {
                        // Set<IRVariableOperand> uses = IRUtil.getSourceOperands(path.get(idx));
                        // if (!uses.isEmpty() && uses.contains((IRVariableOperand) j.operands[0]))
                        //     foundUseBeforeRedef = true;
                    }

                    if (IRUtil.isDefinition(path.get(idx))) {
                        // if (((IRVariableOperand) path.get(idx).operands[0]).getName().equals(defVar)) {
                        //     jKilled = true;
                        //     break;
                        // }
                    }
                    
                }
                // jReachedi = jReachedi && !jKilled;
            }
        }
    }

    public static boolean isReachingDef2(IRInstruction i, IRInstruction j, 
            IRFunction f, ControlFlowGraph cfg) {
        // boolean reaches = false;
        String defVar = ((IRVariableOperand) j.operands[0]).getName();
        BasicBlockBase iB = i.belongsToBlock;
        BasicBlockBase jB = j.belongsToBlock;

        // Map<String, Set<IRInstruction>> globalDefMap = new HashMap<>();
        // Map<String, Set<IRInstruction>> globalUseMap = new HashMap<>();
        // for (BasicBlockBase block : cfg.getBlocks()) {
        //     for (String defOperand : block.operandDefs.keySet()) {
        //         if (!globalDefMap.containsKey(defOperand)) 
        //             globalDefMap.put(defOperand, block.operandDefs.get(defOperand));
        //         else
        //             globalDefMap.get(defOperand).addAll(block.operandDefs.get(defOperand));
        //     }
        //     for (String useOperand : block.operandUses.keySet()) {
        //         if (!globalUseMap.containsKey(useOperand)) 
        //             globalUseMap.put(useOperand, block.operandUses.get(useOperand));
        //         else
        //             globalUseMap.get(useOperand).addAll(block.operandUses.get(useOperand));
        //     }
        // }
        // System.out.println("\n\n<<<  GLOBAL DEF MAP >>>");
        // for (String key : globalDefMap.keySet()) {
        //     System.out.println("Var \""+key+"\" defined by:");
        //     for (IRInstruction inst : globalDefMap.get(key)) {
        //         System.out.print("\t["+inst.belongsToBlock+"] "/*"+inst.irLineNumber+".*/+"\t");
        //         debugPrinter.printInstruction(inst);
        //     }
        // }
        // System.out.println("\n\n<<<  GLOBAL USE MAP >>>");
        // for (String key : globalUseMap.keySet()) {
        //     System.out.println("Var \""+key+"\" used by:");
        //     for (IRInstruction inst : globalUseMap.get(key)) {
        //         System.out.print("\t["+inst.belongsToBlock+"] "/*+inst.irLineNumber+".*/+"\t");
        //         debugPrinter.printInstruction(inst);
        //     }
        // }


        // If there is another definition, d, of defVar AFTER j and BEFORE i
        // and there are no uses of defVar on the path from j --> i, then
        // j does not reach i.
        // for (IRInstruction definition : globalDefMap.get(defVar)) {
        //     if (definition.irLineNumber < i.irLineNumber 
        //             && i.predecessors.contains(definition.belongsToBlock)) {

        //     }
        // }

        boolean checked = false;
        boolean jReachedi = true;   // Must be true for ALL paths
        for (List<BasicBlockBase> pathFromRoot : iB.pathsFromRoot) {
            if (pathFromRoot.indexOf(jB) < 0) continue;     // Look at paths to i containing j's block only

            // MaxBasicBlock path[((MaxBasicBlock)iB).instructions.size()]; //= /*(BasicBlockBase[])*/ 
            // pathFromRoot.toArray(path);
            // for (int n = jIdx; n < path.length; n++) {
            // }

            List<IRInstruction> path = new ArrayList<>();   // All instructions on path
            boolean jVisited = false;
            for (BasicBlockBase bb : pathFromRoot) {
                if (!jVisited) {
                    if (bb.equals(jB)) {
                        // boolean ignoreInstructions = true;
                        boolean addInstructions = false;
                        for (IRInstruction instruction : ((MaxBasicBlock) bb).instructions) {
                            // Ignore adding j block's instructions until j itself is encountered
                            addInstructions = addInstructions || instruction.equals(j);
                            if (addInstructions) 
                                path.add(instruction);
                            // if (ignoreInstructions) {
                            //     if (instruction.equals(j)) ignoreInstructions = false;  
                            // }
                            // if (!ignoreInstructions) path.add(instruction);
                        }
                        jVisited = true;
                        continue;
                    }
                }
                if (jVisited) {
                    if (bb.equals(iB)) {
                        boolean ignoreInstructions = false;
                        for (IRInstruction instruction : ((MaxBasicBlock) bb).instructions) {
                            // Add only instructions up to and including i -- none after i
                            if (!ignoreInstructions) {
                                path.add(instruction);
                                if (instruction.equals(i))  
                                    ignoreInstructions = true;
                            }
                        }
                    }
                    else 
                        path.addAll(((MaxBasicBlock) bb).instructions);
                }
            }

            int jIdx = path.indexOf(j);     // Should be 0
            if (jIdx < 0) continue;         // Something went wrong and j was not found

            // boolean foundUseBeforeRedef = false;
            boolean jKilled = false;

            // Walk every instruction from j to i (exclusive) 
            for (int idx = jIdx+1; idx < path.size()-1; idx++) {

                /* Q: Does it even matter if there are any uses of defVar between j and a redefinition??? */
                // if (!jKilled) {
                //     Set<IRVariableOperand> uses = IRUtil.getSourceOperands(path[idx]);
                //     if (!uses.isEmpty() && uses.contains((IRVariableOperand) j.operands[0]))
                //         foundUseBeforeRedef = true;
                // }

                if (IRUtil.isDefinition(path.get(idx))) {
                    if (((IRVariableOperand) path.get(idx).operands[0]).getName().equals(defVar)) {
                        jKilled = true;
                        // break;
                    }
                }
                
            }
            jReachedi = jReachedi && !jKilled;
            checked = true;
        }

        return checked ? jReachedi : false;
    }

    /**    
        A definition "j" reaches instruction "i" iff:
            1)  "j" is in the IN set for the basic block B(i) containing "i", and
            2)  the def is not killed locally within B(i) before instruction "i"
    **/
    public static boolean isReachingDef(IRInstruction i, IRInstruction j, IRFunction f, ControlFlowGraph cfg) {
        BasicBlockBase iB = i.belongsToBlock;
        BasicBlockBase jB = j.belongsToBlock;

        if (i.irLineNumber < j.irLineNumber && iB.equals(jB) && cfg.blockHasSingleEntry(iB))
            return false;

        // Check all instructions in i's basic block preceeding instruction i
        // IRInstruction instructions[] = (((MaxBasicBlock) iB).instructions.toArray(new IRInstruction[iB.size]));
        IRVariableOperand defVar = (IRVariableOperand) j.operands[0];
        boolean reaches = true;
        boolean usedBeforeKilled = false;
        boolean defKill = true;
        IRInstruction k = null;

        IRPrinter debugPrinter = new IRPrinter(System.out);
        boolean debug = VERBOSE_REACHING_DEFS && false; //j.irLineNumber == 11;
        if (debug)
            System.out.println("[isReachingDef] DEBUG FOR  'ASSIGN, T, 0.'");


        for (IRInstruction inst : ((MaxBasicBlock) iB).instructions) {
        // List<IRInstruction> instructions = new ArrayList<>();
        // if (iB.iDom != null)
        //     instructions.addAll(((MaxBasicBlock) iB.iDom).instructions);
        // instructions.addAll(((MaxBasicBlock) iB).instructions);
        // for (IRInstruction inst : instructions) {

            if (debug) {
                System.out.print("--> inspecting line "+inst.irLineNumber+" for DEBUG:\t");
                debugPrinter.printInstruction(inst);
            }

            if (iB.kill.contains(j) && iB.in.contains(j) && !iB.out.contains(j) && !iB.gen.contains(j)) {
                return false;
            }

            if (inst.equals(i)) {
                if (debug)
                    System.out.println("--> breaking for '(instructions[idx].equals(i))'");
                break;          // Stop once instruction i is reached
                // inst = IRUtil.getInstructionAfterThis(f, inst);
                // continue;
            }
            if (inst.equals(j)) {
                if (debug)
                    System.out.println("--> continuing for '(instructions[idx].equals(j))'");
                inst = IRUtil.getInstructionAfterThis(f, inst);
                continue;
            }
            if (inst.irLineNumber < j.irLineNumber && iB.equals(jB)) {
                if (debug)
                    System.out.println("--> continuing for '(instructions[idx].irLineNumber < j.irLineNumber && iB.equals(jB))'");
                inst = IRUtil.getInstructionAfterThis(f, inst);
                continue;
                // break;
            }

            if (IRUtil.isDefinition(inst)) {
                if (debug) {
                    System.out.print("--> inspecting line "+inst+" for defKill:\t");
                    debugPrinter.printInstruction(inst);
                }

                // Check if this instruction will kill j's definition
                IRVariableOperand lhs = (IRVariableOperand) inst.operands[0];
                if (lhs.getName().equals(defVar.getName())) {
                    if (debug) 
                        System.out.println("--> found common LHS:\t"+lhs);
                    

                    // boolean usedBeforeKilled = false;
                    // boolean defKill = true;

                    // Finally, verify this instruction doesn't use j's definition before killing it
                    for (IRVariableOperand rhs : IRUtil.getSourceOperands(inst)) {
                        if (rhs.getName().equals(defVar.getName()))
                            // defKill = false;
                            usedBeforeKilled = true;
                    }

                    // if (!usedBeforeKilled) {
                    //     defKill = true;
                    if (usedBeforeKilled) 
                        defKill = false;
                    else
                        k = inst;
                    
                }
            }
            // inst = IRUtil.getInstructionAfterThis(f, inst);
        // }
        }

        if (defKill && k != null) {    // If the definition is killed locally in i's block
            if (VERBOSE_REACHING_DEFS) {
                System.out.println("[isReachingDef] Instruction 'i' NOT REACHED by def 'j' -- killed by redef 'k'");
                System.out.print(">>>  k ("+k.belongsToBlock+", line "+k.irLineNumber+"):\t");
                debugPrinter.printInstruction(k);
                System.out.println();
            }
            reaches = false;
            // break;
            // return false;
        }
        return reaches;
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
