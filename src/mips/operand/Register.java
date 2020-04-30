package mips.operand;

public class Register extends MIPSOperand {

    public String name;
    public boolean isVirtual;
    public boolean inUse;
    public boolean prevInUse;   //// TODO: Perhaps make this a stack for the sake of recursive calls
    // List<MIPSInstruction> references;
    public boolean reservedForSpill;

    public Register(String name) {
        this(name, true);
    }

    public Register(String name, boolean isVirtual) {
        this.name = name;
        this.isVirtual = isVirtual;
        this.inUse = false;
        this.prevInUse = false;
        this.reservedForSpill = false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Register)) {
            return false;
        }

        if (((Register)other).name.equals(name)) {
            return true;
        } else {
            return false;
        }
    }
}
