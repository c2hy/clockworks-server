package io.github.c2hy.clockworks.base;

import io.github.c2hy.clockworks.timer.*;
import io.github.c2hy.clockworks.timerbundle.TimerBundleRepository;
import io.github.c2hy.clockworks.timerbundle.TimerRegisterDTO;
import io.github.c2hy.clockworks.timerbundle.TimerRegisterHandler;
import io.github.c2hy.clockworks.timerbundle.TimerRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public abstract class ServerFactory {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    protected abstract TimerRepository timerRepository();

    protected abstract TimerCallback timerCallback();

    protected abstract TimerBundleRepository timerBundleRepository(TimerRepository timerRepository);

    public ServerStartable createServer() {
        var timerRepository = this.timerRepository();
        var timerCallback = this.timerCallback();
        var timerBundleRepository = this.timerBundleRepository(timerRepository);

        var timerTriggerService = new TimerTriggerService();
        var timerTriggerHandler = new TimerTriggerHandler(timerTriggerService, timerCallback, timerRepository);
        var timerRegisterService = new TimerRegisterService(timerBundleRepository);
        var timerRegisterHandler = new TimerRegisterHandler(timerRegisterService, timerBundleRepository);

        var actionDefinable = new UndertowServer()
                .action(
                        "timer-bundle-register",
                        TimerRegisterDTO.class,
                        timerRegisterHandler::handle
                );

        return () -> {
            actionDefinable.ready();
            schedule(timerRepository, timerTriggerHandler);
        };
    }

    private void schedule(TimerRepository timerRepository,
                          TimerTriggerHandler timerTriggerHandler) {
        executor.scheduleAtFixedRate(() -> {
            for (var timer : timerRepository.timers(100)) {
                var timerTriggerDTO = new TimerTriggerDTO();
                timerTriggerDTO.setId(timer.getId());
                try {
                    timerTriggerHandler.handle(timerTriggerDTO);
                } catch (Exception e) {
                    log.error("handle timer trigger error", e);
                }
                timerRepository.deleteById(timer.getId());
            }
        }, 1, 5, TimeUnit.SECONDS);
    }
}
