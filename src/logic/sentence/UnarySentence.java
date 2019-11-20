package logic.sentence;

import logic.base.Sentence;
import logic.base.Operator;

public class UnarySentence implements Sentence {
    private Sentence a;
    private Operator op;

    public UnarySentence(Sentence a, Operator op) {
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
