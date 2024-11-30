package io.github.c2hy.clockworks.base.adapter;

import io.github.c2hy.clockworks.timer.Timer;
import io.github.c2hy.clockworks.timer.TimerRepository;
import io.github.c2hy.clockworks.timerbundle.TimerBundle;
import io.github.c2hy.clockworks.timerbundle.TimerBundleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MemoryTimerBundleRepository implements TimerBundleRepository {
    private final AtomicLong idGenerator = new AtomicLong();
    private final Map<String, TimerBundle> timerBundleStoreByCode = new ConcurrentHashMap<>();

    private final TimerRepository timerRepository;

    @Override
    public TimerBundle findByCode(String code) {
        var timerBundle = timerBundleStoreByCode.get(code);
        if (timerBundle == null) {
            return null;
        }

        var timers = timerRepository.findByTimerBundleId(timerBundle.getId());
        return new TimerBundle(
                timerBundle.getId(),
                timerBundle.getCode(),
                timerBundle.getLastRegisterTime(),
                timers,
                Collections.emptyList()
        );
    }

    @Override
    public void save(TimerBundle timerBundle) {
        var timerBundleId = idGenerator.incrementAndGet();

        timerBundleStoreByCode.put(timerBundle.getCode(), new TimerBundle(
                timerBundleId,
                timerBundle.getCode(),
                timerBundle.getLastRegisterTime(),
                Collections.emptyList(),
                Collections.emptyList()
        ));

        var deletedTimer = timerBundle.getDeletedTimer();
        deletedTimer.stream().map(Timer::getId).forEach(timerRepository::deleteById);
        log.debug("deleted timers: {}", deletedTimer);

        var timers = timerBundle.getTimers()
                .stream()
                .map(v -> v.withTimerBundleId(timerBundleId))
                .collect(Collectors.toList());
        timers.forEach(timerRepository::save);
        log.debug("saved timers: {}", timers);
    }
}
