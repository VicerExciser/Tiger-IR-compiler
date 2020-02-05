import ir.*;
import ir.datatype.IRArrayType;
import ir.datatype.IRIntType;
import ir.datatype.IRType;
import ir.operand.IRConstantOperand;
import ir.operand.IROperand;
import ir.operand.IRVariableOperand;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class IROptimizer {

    ////  TODO  ////

    public static void main(String[] args) throws Exception {
        // Parse the IR file
        IRReader irReader = new IRReader();
        IRProgram program = irReader.parseIRFile(args[0]);

        
        // TODO
        
        // Looks like we can follow the main logic of Demo.java on determining variables and unused vars.
        // Let's start with a single pass of non-reaching dead code, then move from there.
        
        // Print the IR to another file
        IRPrinter filePrinter = new IRPrinter(new PrintStream(args[1]));
        filePrinter.printProgram(program);

    }

    ////  TODO  ////

}
