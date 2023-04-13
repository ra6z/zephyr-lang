package io.ra6.zephyr;

public final class Iterables {
    private Iterables(){}
    public static <T> T first(Iterable<T> elements) {
        return elements.iterator().next();
    }

    public static <T> T last(Iterable<T> elements) {
        T lastElement = null;

        for (T element : elements) {
            lastElement = element;
        }

        return lastElement;
    }
}
