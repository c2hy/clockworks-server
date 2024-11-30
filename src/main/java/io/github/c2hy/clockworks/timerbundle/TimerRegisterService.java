package io.github.c2hy.clockworks.timerbundle;

import lombok.RequiredArgsConstructor;

import java.util.Collections;

@RequiredArgsConstructor
public class TimerRegisterService {
    private final TimerBundleRepository timerBundleRepository;

    public TimerBundle create(String timerBundleCode) {
        var timerBundleExists = timerBundleRepository.findByCode(timerBundleCode);
        if (timerBundleExists == null) {
            return new TimerBundle(
                    null,
                    timerBundleCode,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }

        return new TimerBundle(
                timerBundleExists.getId(),
                timerBundleExists.getCode(),
                timerBundleExists.getLastRegisterTime(),
                timerBundleExists.getTimers(),
                Collections.emptyList()
        );
    }
}
