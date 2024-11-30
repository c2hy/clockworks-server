package io.github.c2hy.clockworks.base.adapter;

import io.github.c2hy.clockworks.timer.TimerCallback;

public class DoNothingCallback implements TimerCallback {
    @Override
    public void callback() {
        System.err.println("Do nothing callback");
    }
}
