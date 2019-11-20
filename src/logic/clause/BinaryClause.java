package logic.clause;

import logic.base.Clause;
import logic.base.Operator;

public class BinaryClause implements Clause {
    private Clause a;
    private Clause b;
    private Operator op;

    public BinaryClause(Clause a, Operator op, Clause b) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    public boolean value() {
        return this.op.eval(a.value(), b.value());
    }

    @Override
    public String toString() {
        return "(" + this.a.toString() + this.op.ch + this.b.toString() + ")";
    }
}
