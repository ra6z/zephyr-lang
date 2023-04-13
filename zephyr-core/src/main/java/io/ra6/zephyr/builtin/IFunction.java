package io.ra6.zephyr.builtin;

import java.util.HashMap;

public interface IFunction {
    Object invoke(HashMap<String, Object> args);
}
