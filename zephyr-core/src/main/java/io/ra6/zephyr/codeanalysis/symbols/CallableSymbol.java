package io.ra6.zephyr.codeanalysis.symbols;

import java.util.List;

public abstract class CallableSymbol extends Symbol {
    public CallableSymbol(String name) {
        super(name);
    }

    public abstract List<ParameterSymbol> getParameters();


    public boolean isParameterDeclared(String name) {
        for (ParameterSymbol parameter : getParameters()) {
            if (parameter.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ParameterSymbol getParameter(String name) {
        for (ParameterSymbol parameter : getParameters()) {
            if (parameter.getName().equals(name)) {
                return parameter;
            }
        }
        return null;
    }
}
