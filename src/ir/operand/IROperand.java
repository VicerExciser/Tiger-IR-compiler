package ir.operand;

import ir.IRInstruction;

/*  Inheriting subclasses:      (all have a '.getName()'' instance method)

        - IRLabelOperand        
        - IRFunctionOperand
        - IRVariableOperand     (has public instance variable 'IRType type')
        - IRConstantOperand     (has public instance variable 'IRType type')
*/

public abstract class IROperand {

    protected String value;

    protected IRInstruction parent;

    public IROperand(String value, IRInstruction parent) {
        this.value = value;
        this.parent = parent;
    }

    public IRInstruction getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return value;
    }

}
