package io.ra6.zephyr.runtime;

import java.util.Stack;

public class VariableTableStack extends Stack<VariableTable> {

    @Override
    public VariableTable push(VariableTable item) {
        //System.out.println("Entering: " + item);
        return super.push(item);
    }

    @Override
    public synchronized VariableTable pop() {
        //System.out.println("Exiting: " + peek());
        return super.pop();
    }
}
