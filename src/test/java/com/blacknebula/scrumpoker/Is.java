package com.blacknebula.scrumpoker;

/**
 * @author hazem
 */
public class Is<T> {
    private T value;

    public Is(T value) {
        this.value = value;
    }

    public static <T> Is<T> is(T value) {
        return new Is<T>(value);
    }

    public boolean in(T... set) {
        for (T item : set) {
            if (value.equals(item)) {
                return true;
            }
        }

        return false;
    }
}