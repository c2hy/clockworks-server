package io.github.c2hy.clockworks.base.adapter;

import io.github.c2hy.clockworks.timer.Timer;
import io.github.c2hy.clockworks.timer.TimerRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MemoryTimerRepository implements TimerRepository {
    private final AtomicLong idGenerator = new AtomicLong();
    private final Map<Long, Timer> timerStore = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> timerBundleBoundStore = new ConcurrentHashMap<>();

    @Override
    public void save(Timer timer) {
        var timerId = idGenerator.incrementAndGet();
        var timerBundleId = timer.getTimerBundleId();

        timerStore.put(timerId, timer.withId(timerId));

        var ids = timerBundleBoundStore.computeIfAbsent(timerBundleId, k -> new ArrayList<>());
        timerBundleBoundStore.put(timerBundleId, Stream.concat(ids.stream(), Stream.of(timerId)).collect(Collectors.toList()));
    }

    @Override
    public void deleteById(Long timerId) {
        timerStore.remove(timerId);
    }

    @Override
    public List<Timer> findByTimerBundleId(Long id) {
        var ids = timerBundleBoundStore.getOrDefault(id, Collections.emptyList());
        return ids.stream().map(timerStore::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<Timer> timers(int limit) {
        return timerStore.values().stream().limit(limit).toList();
    }
}
