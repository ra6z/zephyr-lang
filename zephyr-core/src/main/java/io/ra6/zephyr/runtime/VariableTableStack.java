package io.ra6.zephyr.runtime;

import java.util.Stack;

public class VariableTableStack extends Stack<VariableTable> {
    private static int indent = 0;

    @Override
    public VariableTable push(VariableTable item) {
        RuntimeLogger.printf("%sEntering: %s%n", " ".repeat(indent), item);
        indent += 4;
        return super.push(item);
    }

    @Override
    public synchronized VariableTable pop() {
        indent -= 4;
        RuntimeLogger.printf("%sLeaving:  %s%n", " ".repeat(indent), peek());
        return super.pop();
    }
}
