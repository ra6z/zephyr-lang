package io.ra6.zephyr;

import lombok.Data;

@Data
public class Tuple<TItem1, TItem2> {
    private final TItem1 item1;
    private final TItem2 item2;
}
