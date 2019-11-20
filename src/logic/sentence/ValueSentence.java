package logic.sentence;

import logic.base.Sentence;

import java.util.concurrent.atomic.AtomicBoolean;

public class ValueSentence implements Sentence {
    private final String name;
    private final AtomicBoolean value;

    public ValueSentence(String name, AtomicBoolean value) {
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
