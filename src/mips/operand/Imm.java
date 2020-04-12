package mips.operand;

public class Imm extends MIPSOperand {

    private String val;
    private String type;

    public Imm(String val) {
        this(val, "DEC");
    }

    public Imm(String val, String type) {
        this.val = val;
        this.type = type;

        if (val.length() > 1 && "0x".equals(val.substring(0, 2))) {
            // this.val = val.substring(val.indexOf('x') + 1);
            this.type = "HEX";
        }
        if (val.indexOf('x') >= 0) {
            this.type = "HEX";
        }
    }

    public int getInt() {
        switch (type) {
            case "HEX":
                return Integer.decode(val);
            case "DEC":
                return Integer.parseInt(val);
            default:
                return 0;
        }
    }

    public float getSingle() {
        if (type.equals("SINGLE")) {
            return Float.parseFloat(val);
        }

        throw new IllegalArgumentException();
    }

    public double getDouble() {
        if (type.equals("DOUBLE")) {
            return Double.parseDouble(val);
        }

        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return val;
    }
}
