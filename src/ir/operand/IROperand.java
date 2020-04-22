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

    @Override
    public boolean equals(Object obj) {
        return obj != null
            && obj instanceof IROperand
            && (this == obj
            || (((IROperand)obj).value.equals(this.value)
            && ((IROperand)obj).getParent().irLineNumber == (getParent().irLineNumber)));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getParent().irLineNumber;
        result = 31 * result + this.value.toLowerCase().hashCode();
        return result;
    }

}
