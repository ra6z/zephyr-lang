package io.ra6.zephyr.builtin;

import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;

import java.util.List;

public abstract class InternalFunctionBase {
    public abstract IFunction getFunctionBody();
    public abstract int getArity();
    public abstract List<ParameterSymbol> getParameters();
}
