package io.github.c2hy.clockworks.timer;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TimerDefinitionDTO {
    private String code;
    private String callbackRouting;
    private String cronExpression;
    private OffsetDateTime beginAt;
    private OffsetDateTime finishAt;
}
