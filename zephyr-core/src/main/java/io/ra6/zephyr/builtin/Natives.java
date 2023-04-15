package io.ra6.zephyr.builtin;

import io.ra6.zephyr.builtin.natives.NativeConsole;
import io.ra6.zephyr.builtin.natives.NativeType;

import java.util.HashMap;

public class Natives {
    private static final HashMap<String, NativeType> TYPES = new HashMap<>();

    static {
        register(new NativeConsole());
    }

    public static void register(NativeType type) {
        TYPES.put(type.getNativeName(), type);
    }

    public static NativeType getNativeType(String name) {
        return TYPES.get(name);
    }
}
