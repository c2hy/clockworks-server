package io.github.c2hy.clockworks.timerbundle;

public interface TimerBundleRepository {
    void save(TimerBundle timerBundle);

    TimerBundle findByCode(String code);
}
