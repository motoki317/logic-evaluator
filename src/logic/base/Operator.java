package logic.base;

public enum Operator {
    NOT('¬', "~", "not"),
    AND('∧', "/\\", "&", "and"),
    OR('∨', "\\/", "|", "or"),
    IMPLY('⇒', "→", "->", "=>"),
    EQUIV('⇔', "↔", "<->", "<=>", "="),;

    public final char ch;
    public final String[] replaceableTexts;

    Operator(char ch, String... replaceable) {
        this.ch = ch;
        this.replaceableTexts = replaceable;
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
