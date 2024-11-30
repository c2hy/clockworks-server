package io.github.c2hy.clockworks.timerbundle;

import io.github.c2hy.clockworks.common.Args;
import io.github.c2hy.clockworks.common.Logs;
import io.github.c2hy.clockworks.timer.Timer;
import io.github.c2hy.clockworks.timer.TimerDefinitionDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TimerRegisterHandler {
    private final TimerRegisterService timerRegisterService;
    private final TimerBundleRepository timerBundleRepository;

    public TimerRegisterHandler(TimerRegisterService timerRegisterService,
                                TimerBundleRepository timerBundleRepository) {
        this.timerRegisterService = timerRegisterService;
        this.timerBundleRepository = timerBundleRepository;
    }

    public TimerRegisterResultDTO handle(TimerRegisterDTO timerRegisterDTO) {
        var timerBundleCode = timerRegisterDTO.getTimerBundleCode();
        Logs.trace(timerBundleCode);

        if (log.isDebugEnabled()) {
            log.debug("handle timer register: {}", timerRegisterDTO);
        } else {
            log.info("handle timer register");
        }

        Args.checkNotEmpty(timerBundleCode, "timerBundleCode");

        var timerDefinitions = timerRegisterDTO.getTimerDefinitions();
        Args.checkNotEmpty(timerDefinitions, "timerDefinitions");

        var timerBundle = timerRegisterService.create(timerBundleCode);

        var registerCooldownSeconds = timerRegisterDTO.getRegisterCooldownSeconds();
        var cooldown = timerBundle.isCooldown(registerCooldownSeconds);
        if (cooldown) {
            log.info("timer register cooldown: {}", registerCooldownSeconds);
            return TimerRegisterResultDTO.cooldown();
        }

        var timers = this.createTimer(timerRegisterDTO.getTimerDefinitions());
        log.debug("registering timers: {}", timers);

        var registeredTimerBundle = timerBundle.register(timers);
        timerBundleRepository.save(registeredTimerBundle);
        log.info("timer register success");

        return TimerRegisterResultDTO.success();
    }

    private List<Timer> createTimer(List<TimerDefinitionDTO> timerDefinitions) {
        return timerDefinitions
                .stream()
                .map(v -> Timer.create(
                        v.getCode(),
                        v.getCallbackRouting(),
                        v.getCronExpression(),
                        v.getBeginAt(),
                        v.getFinishAt()
                ))
                .collect(Collectors.toList());
    }
}
