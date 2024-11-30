package io.github.c2hy.clockworks.timerbundle;

import io.github.c2hy.clockworks.timer.Timer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class TimerBundle {
    private final Long id;
    private final String code;
    private final OffsetDateTime lastRegisterTime;
    private final List<Timer> timers;
    private final List<Timer> deletedTimer;

    public boolean isCooldown(int registerCooldownSeconds) {
        return Duration.between(lastRegisterTime, OffsetDateTime.now()).toSeconds() < registerCooldownSeconds;
    }

    public TimerBundle register(List<Timer> timers) {
        return new TimerBundle(
                null,
                code,
                OffsetDateTime.now(),
                timers, this.timers
        );
    }
}
