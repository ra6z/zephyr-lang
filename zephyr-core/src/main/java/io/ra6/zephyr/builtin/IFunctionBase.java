package io.ra6.zephyr.builtin;

import io.ra6.zephyr.codeanalysis.symbols.ParameterSymbol;

import java.util.List;

public interface IFunctionBase {
    String PARAM_OTHER = "other";
    String PARAM_THIS = "this";

    ICallable getFunctionBody();
    int getArity();
    List<ParameterSymbol> getParameters();
}
