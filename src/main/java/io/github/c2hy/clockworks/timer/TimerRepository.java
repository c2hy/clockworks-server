package io.github.c2hy.clockworks.timer;

import java.util.List;

public interface TimerRepository {
    void save(Timer timer);

    void deleteById(Long timerId);

    List<Timer> findByTimerBundleId(Long id);

    List<Timer> timers(int limit);
}
