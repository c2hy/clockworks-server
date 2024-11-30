package io.github.c2hy.clockworks.base.adapter;

import io.github.c2hy.clockworks.base.ServerFactory;
import io.github.c2hy.clockworks.timer.TimerCallback;
import io.github.c2hy.clockworks.timer.TimerRepository;
import io.github.c2hy.clockworks.timerbundle.TimerBundleRepository;

public class MemoryServerFactory extends ServerFactory {
    @Override
    protected TimerRepository timerRepository() {
        return new MemoryTimerRepository();
    }

    @Override
    protected TimerCallback timerCallback() {
        return new DoNothingCallback();
    }

    @Override
    protected TimerBundleRepository timerBundleRepository(TimerRepository timerRepository) {
        return new MemoryTimerBundleRepository(timerRepository);
    }
}
