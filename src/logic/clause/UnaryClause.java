package logic.clause;

import logic.base.Clause;
import logic.base.Operator;

public class UnaryClause implements Clause {
    private Clause a;
    private Operator op;

    public UnaryClause(Clause a, Operator op) {
        this.a = a;
        this.op = op;
    }

    public boolean value() {
        return this.op.eval(a.value());
    }

    @Override
    public String toString() {
        return "(" + this.op.ch + this.a.toString() + ")";
    }
}
