package logic.clause;

import logic.base.Clause;

import java.util.concurrent.atomic.AtomicBoolean;

public class ValueClause implements Clause {
    private final String name;
    private final AtomicBoolean value;

    public ValueClause(String name, AtomicBoolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean value() {
        return this.value.get();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
