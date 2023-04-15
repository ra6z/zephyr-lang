package io.ra6.zephyr.codeanalysis.symbols;

import io.ra6.zephyr.builtin.Types;
import io.ra6.zephyr.codeanalysis.binding.Visibility;
import lombok.Getter;

import java.util.List;

public class ArrayTypeSymbol extends TypeSymbol {
    @Getter
    private final TypeSymbol elementType;

    public ArrayTypeSymbol(TypeSymbol elementType) {
        super(elementType.getName() + "[]");
        this.elementType = elementType;

        setFieldsAndFunctions(List.of(
                new FunctionSymbol("copy", false, Visibility.PUBLIC, List.of(), this),
                new FieldSymbol("length", true, true, Visibility.PUBLIC, Types.INT)
        ));
    }

    @Override
    public void setFieldsAndFunctions(List<Symbol> fieldsAndFunctions) {
        super.setFieldsAndFunctions(fieldsAndFunctions);
    }
}
