package io.ra6.zephyr.builtin;

import io.ra6.zephyr.codeanalysis.binding.Visibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Builtin {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Type {
        String typeName();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {
        String fieldName();
        String fieldType();

        Visibility visibility() default Visibility.PRIVATE;
        boolean shared() default false;
        boolean readonly() default false;
    }
}
