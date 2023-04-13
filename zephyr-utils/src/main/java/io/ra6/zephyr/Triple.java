package io.ra6.zephyr;

import lombok.Data;

@Data
public class Triple<Item1, Item2, Item3> {
    private final Item1 item1;
    private final Item2 item2;
    private final Item3 item3;
}
