package io.github.c2hy.clockworks.timerbundle;

import io.github.c2hy.clockworks.timer.TimerDefinitionDTO;
import lombok.Data;

import java.util.List;

@Data
public class TimerRegisterDTO {
    private String timerBundleCode;
    private int registerCooldownSeconds;
    private List<TimerDefinitionDTO> timerDefinitions;
}
