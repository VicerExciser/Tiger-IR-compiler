package mips;

import mips.cfg.*;
import mips.operand.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

public class MIPSInstruction {

    public static final int WORD_SIZE = 4;

    // binary operation operand order
    public static final int R_D_BOP = 0;
    public static final int R_S_BOP = 1;
    public static final int R_T_BOP = 2;

    // operand order for LI
    public static final int R_D_LI = 0;
    public static final int IMM_LI = 1;

    // operand order for LW
    public static final int R_D_LW = 0;
    public static final int ADDR_LW = 1;

    // operand order for MOVE
    public static final int R_D_MOV = 0;
    public static final int R_S_MOV = 1;

    // operand order for SW
    public static final int R_S_SW = 0;
    public static final int ADDR_SW = 1;

    // operand order for branches
    public static final int R_S_BR = 0;
    public static final int R_T_BR = 1;
    public static final int LABEL_BR = 2;

    //operand order for J
    public static final int LABEL_J = 0;

    // operand order for JR
    public static final int R_S_JR = 0;


    public MIPSOp op;
    public String label;
    public List<MIPSOperand> operands;
    public MIPSBlock parentBlock;       //// parentBlock assigned when the MIPSInstruction is appended to a MIPSBlock
    public boolean usesVirtualReg;
    public List<Register> virtualRegOperands;

    //// This structure will map the name of a MIPSOperand (i.e., Register) to the name of
    //// the original IROperand (i.e., IRVariableOperand) for which the register was allocated
    public Map<String, String> associatedNames;


    public MIPSInstruction(MIPSOp op, String label, MIPSOperand... operands) {
        this.op = op;
        this.label = label;
        this.parentBlock = null;

        if (operands == null) {
            this.operands = new ArrayList<>();
        } else {
            switch(op) {
                case DIRECTIVE:
                case SYSCALL:
                case LABEL:
                case NOP:
                    this.operands = new ArrayList<>();
                    break;
                default:
                    this.operands = Arrays.asList(operands);
            }
        }
        
        this.usesVirtualReg = false;
        this.virtualRegOperands = null;
        if (!this.operands.isEmpty()) {
            for (MIPSOperand operand : this.operands) {
                if (operand instanceof Register) {
                    if (((Register) operand).isVirtual) {
                        if (!this.usesVirtualReg) {
                            this.usesVirtualReg = true;
                        }
                        if (this.virtualRegOperands == null) {
                            this.virtualRegOperands = new ArrayList<>();
                        }
                        this.virtualRegOperands.add((Register) operand);
                    }
                }
            }
        }

        this.associatedNames = hasVariableOperands() ? new HashMap<>() : null;

    }

    public boolean isBranch() {
        switch (op) {
            case BEQ:
            case BNE:
            case BLT:
            case BGT:
            case BGE:
                return true;
            default:
                return false;
        }
    }

    public boolean hasVariableOperands() {
        // if (label != null) return false;
        switch(op) {
            case DIRECTIVE:
            case SYSCALL:
            case LABEL:
            case NOP:
            case JAL:
            case J:
                return false;
            default:
                /*
                return label == null;
                if (label != null) {
                    return false;
                }
                */
                return true;
        }
    }

    @Override
    public String toString() {
        if (op == MIPSOp.NOP) {
            return op.toString();
        }

        StringBuilder builder = new StringBuilder();

        if (label != null) {
            if (op == MIPSOp.DIRECTIVE) {
                builder.append(label);
                return builder.toString();
            }
            if (op == MIPSOp.LABEL) {
                builder.append(label + ": ");
                return builder.toString();
            }
            if (op == MIPSOp.COMMENT) {
                builder.append("    # ");
                builder.append(label);
                return builder.toString();
            }
        }
        builder.append("    "); // Indent for non-labels
        builder.append(op.toString());
        if (operands.isEmpty()) {
            return builder.toString();
        }
        builder.append(' ');

        for (int i = 0; i < operands.size() - 1; i++) {
            MIPSOperand operand = operands.get(i);
            builder.append(operand.toString());
            builder.append(", ");
        }

        builder.append(operands.get(operands.size() - 1));

        return builder.toString();
    }


    public String getDefinedName() {
        Register definition = getWrite();
        if (definition == null) return null;
        return this.associatedNames.get(definition.name);
    }

    public String[] getUsedNames() {
        Register[] sources = getReads();
        String[] names = new String[sources.length];
        // if (sources.length == 0) return names;
        for (int i = 0; i < sources.length; i++) {
            names[i] = this.associatedNames.get(sources[i].name);
        }
        return names;
    }

    public String getNameForReg(Register readReg) {
        return this.associatedNames.get(readReg.name);
    }



    public Register[] getReads() {
        Register[] reads;

        switch (op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case AND:
            case OR:
            case ADD_S:
            case SUB_S:
            case MUL_S:
            case DIV_S:
            case ADD_D:
            case SUB_D:
            case MUL_D:
            case DIV_D:
                reads = new Register[2];
                reads[0] = (Register)operands.get(R_S_BOP);
                reads[1] = (Register)operands.get(R_T_BOP);
                return reads;
            case ADDI:
            case ANDI:
            case ORI:
            case SLL:
            case ADDI_S:
            case ADDI_D:
                reads = new Register[1];
                reads[0] = (Register)operands.get(R_S_BOP);
                return reads;
            case LW:
            case L_S:
            case L_D:
                reads = new Register[1];
                reads[0] = ((Addr)operands.get(ADDR_LW)).register;
                return reads;
            case MOVE:
            case MOV_S:
            case MOV_D:
                reads = new Register[1];
                reads[0] = (Register)operands.get(R_S_MOV);
                return reads;
            case SW:
            case S_S:
            case S_D:
                reads = new Register[2];
                reads[0] = (Register)operands.get(R_S_SW);
                reads[1] = ((Addr)operands.get(ADDR_SW)).register;
                return reads;
            case BEQ:
            case BNE:
            case BLT:
            case BGT:
            case BGE:
            case C_EQ_S:
            case C_NE_S:
            case C_LT_S:
            case C_GT_S:
            case C_GE_S:
            case C_EQ_D:
            case C_NE_D:
            case C_LT_D:
            case C_GT_D:
            case C_GE_D:
                reads = new Register[2];
                reads[0] = (Register)operands.get(R_S_BR);
                reads[1] = (Register)operands.get(R_T_BR);
                return reads;
            case JR:
                reads = new Register[1];
                reads[0] = (Register)operands.get(R_S_JR);
                return reads;
            default:
                return new Register[0];
        }
    }

    public Register getWrite() {
        switch (op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case AND:
            case OR:
            case ADDI:
            case ANDI:
            case ORI:
            case SLL:
            case ADD_S:
            case SUB_S:
            case MUL_S:
            case DIV_S:
            case ADD_D:
            case SUB_D:
            case MUL_D:
            case DIV_D:
                return (Register)operands.get(R_D_BOP);
            case LI:
            case LA:
            case LI_S:
            case LI_D:
                return (Register)operands.get(R_D_LI);
            case LW:
            case L_S:
            case L_D:
                return (Register)operands.get(R_D_LW);
            case MOVE:
            case MOV_S:
            case MOV_D:
                return (Register)operands.get(R_D_MOV);
            default:
                return null;
        }
    }
}
