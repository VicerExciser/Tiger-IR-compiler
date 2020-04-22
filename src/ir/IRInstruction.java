package ir;

import ir.operand.IROperand;
import ir.cfg.BasicBlockBase;

import java.util.List;
import java.util.ArrayList;

public class IRInstruction {

    public enum OpCode {
        ASSIGN,
        ADD, SUB, MULT, DIV, AND, OR,
        GOTO,
        BREQ, BRNEQ, BRLT, BRGT, BRLEQ, BRGEQ,
        RETURN,
        CALL, CALLR,
        ARRAY_STORE, ARRAY_LOAD,
        LABEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public OpCode opCode;

    public IROperand[] operands;

    public int irLineNumber;

    public boolean isLeader;
    public boolean isCondBranchTarget;
    public BasicBlockBase belongsToBlock;

    public List<IRInstruction> reachingDefinitions;

    public IRInstruction() {}

    public IRInstruction(OpCode opCode, IROperand[] operands, int irLineNumber) {
        this.opCode = opCode;
        this.operands = operands;
        this.irLineNumber = irLineNumber;
        this.belongsToBlock = null;
        this.isLeader = false;
        this.isCondBranchTarget = false;
        this.reachingDefinitions = new ArrayList<>();
    }

}
