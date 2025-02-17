package ir.operand;

import ir.IRInstruction;

// For representing function name arguments for CALL and CALLR instructions
public class IRFunctionOperand extends IROperand {

    public IRFunctionOperand(String name, IRInstruction parent) {
        super(name, parent);
    }

    public String getName() {
        return value;
    }
}
