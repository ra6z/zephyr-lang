package io.ra6.zephyr.builtin;

import java.util.HashMap;

public interface ICallable {
    Object call(HashMap<String, Object> args);
}
