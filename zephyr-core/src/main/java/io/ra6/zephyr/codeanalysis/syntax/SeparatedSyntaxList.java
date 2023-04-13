package io.ra6.zephyr.codeanalysis.syntax;

import lombok.Getter;

import java.util.Iterator;
import java.util.List;

public class SeparatedSyntaxList<T extends SyntaxNode> implements Iterable<T> {
    @Getter
    private final List<SyntaxNode> nodesAndSeparators;

    public SeparatedSyntaxList(List<SyntaxNode> nodesAndSeparators) {
        this.nodesAndSeparators = nodesAndSeparators;
    }

    public int count() {
        return (nodesAndSeparators.size() + 1) / 2;
    }

    public T get(int index) {
        return (T) nodesAndSeparators.get(index * 2);
    }

    public SyntaxToken getSeparator(int index) {
        if (index < 0 || index == count() - 1) {
            return null;
        }

        return (SyntaxToken) nodesAndSeparators.get(index * 2 + 1);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count();
            }

            @Override
            public T next() {
                T current = get(index);
                index++;
                return current;
            }
        };
    }
}
