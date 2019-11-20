package logic.sentence;

import logic.base.Sentence;
import logic.base.Operator;

public class BinarySentence implements Sentence {
    private Sentence a;
    private Sentence b;
    private Operator op;

    public BinarySentence(Sentence a, Operator op, Sentence b) {
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
