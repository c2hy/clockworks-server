package io.github.c2hy.clockworks;

import io.github.c2hy.clockworks.base.adapter.MemoryServerFactory;

public class ClockworksServerApplication {
    public static void main(String[] args) {
        new MemoryServerFactory().createServer().start();
    }
}
