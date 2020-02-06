import ir.*;
import ir.cfg.*;
import ir.datatype.*;
import ir.operand.*;
import ir.IRInstruction.OpCode.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

public class IROptimizer {

    ////  TODO  ////
    // Still need to throw some IRExceptions at potential failure points in
    // the basic block classes, CFG classes, and IRUtil to verify our design

    private static void sweep(IRFunction f, Set<IRInstruction> markedSet) {
        //  For each instruction i in function f, if i is not marked, then delete i
        Set<IRInstruction> removeSet = new HashSet<>();
        for (IRInstruction i : f.instructions) {
            if (!markedSet.contains(i))
                removeSet.add(i);
        }

        // Dead code removal from function's instruction list
        for (IRInstruction i : removeSet) 
            f.instructions.remove(i);
    }

    public static void main(String[] args) throws Exception {
        // Parse the IR file
        IRReader irReader = new IRReader();
        IRProgram program = irReader.parseIRFile(args[0]);

        List<ControlFlowGraph> allCFGs = new ArrayList<>(program.functions.size());

        // TODO
        /*
        PriorityQueue(int initialCapacity, Comparator<E> comparator): 
            Creates a PriorityQueue with the specified initial capacity that orders its 
            elements according to the specified comparator.
        */
        IRUtil.InstructionComparator instComparator = new IRUtil.InstructionComparator();
        PriorityQueue<IRInstruction> worklist = new PriorityQueue<>(10, instComparator);
        Set<IRInstruction> marked = new HashSet<>();
        
        // Looks like we can follow the main logic of Demo.java on determining variables and unused vars.
        // Let's start with a single pass of non-reaching dead code, then move from there.
        for (IRFunction function : program.functions) {

            ControlFlowGraph cfg = new ControlFlowGraph(function);
            cfg.build();
            allCFGs.add(cfg);

            for (IRInstruction instruction : function.instructions) {
                // Mark all critical instructions and add to the worklist
                if (IRUtil.isCritical(instruction)) {
                    marked.add(instruction);
                    worklist.add(instruction);
                }
            }


        //// TODO //// --> Use our new CFG and its basic blocks for finding all reaching definitions

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

            sweep(function, marked);
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
        IRPrinter filePrinter = new IRPrinter(new PrintStream(args[1]));
        filePrinter.printProgram(program);

    }

    

    ////  TODO  ////

}
