package io.github.c2hy.clockworks.timer;

import io.github.c2hy.clockworks.common.CronUtils;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.time.OffsetDateTime;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Timer {
    @With
    @Getter
    private final Long id;
    @With
    @Getter
    private final Long timerBundleId;
    @With
    private final OffsetDateTime triggerAt;
    @Getter
    private final String code;
    private final String callbackRouting;
    private final String cronExpression;
    private final OffsetDateTime beginAt;
    private final OffsetDateTime finishAt;

    public static Timer create(String code,
                               String callbackRouting,
                               String cronExpression,
                               @Nullable OffsetDateTime beginAt,
                               @Nullable OffsetDateTime finishAt) {
        return new Timer(
                null,
                null,
                null,
                code,
                callbackRouting,
                cronExpression,
                beginAt,
                finishAt
        );
    }

    @Nullable
    public Timer trigger(TimerCallback timerCallback) {
        timerCallback.callback();

        var now = OffsetDateTime.now();
        if (beginAt != null && now.isBefore(beginAt)) {
            return null;
        }
        if (finishAt != null && now.isAfter(finishAt)) {
            return null;
        }
        var nextTime = CronUtils.next(cronExpression);

        return new Timer(
                null,
                timerBundleId,
                nextTime,
                code,
                callbackRouting,
                cronExpression,
                beginAt,
                finishAt
        );
    }
}
