package logic.base;

public enum Operator {
    NOT('¬'),
    AND('∧'),
    OR('∨'),
    IMPLY('⇒'),
    EQUIV('⇔'),;

    public final char ch;

    Operator(char ch) {
        this.ch = ch;
    }

    /**
     * Get operators in the evaluation order.
     * @return Ordered array of operators.
     */
    public static Operator[] getOrderedValues() {
        return new Operator[]{NOT, AND, OR, IMPLY, EQUIV};
    }

    public boolean eval(boolean a) {
        return eval(a, true);
    }

    public boolean eval(boolean a, boolean b) {
        switch (this) {
            case NOT:
                return !a;
            case AND:
                return a && b;
            case OR:
                return a || b;
            case IMPLY:
                return !a || b;
            case EQUIV:
                return a == b;
            default:
                throw new Error("Unknown operator");
        }
    }
}
