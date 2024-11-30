package io.github.c2hy.clockworks.timer;

import io.github.c2hy.clockworks.common.Logs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimerTriggerHandler {
    private final TimerTriggerService timerTriggerService;
    private final TimerCallback timerCallback;
    private final TimerRepository timerRepository;

    public void handle(TimerTriggerDTO timerTriggerDTO) {
        var timerId = timerTriggerDTO.getId();
        log.info("handle timer trigger {}", timerId);

        var timer = timerTriggerService.create(timerTriggerDTO.getId());
        Logs.trace(timer.getCode());

        var nextTimer = timer.trigger(timerCallback);
        log.info("trigger timer success");

        if (nextTimer != null) {
            log.debug("save next timer");
            timerRepository.save(nextTimer);
        }
    }
}
