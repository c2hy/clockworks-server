package io.github.c2hy.clockworks.timerbundle;

import lombok.Data;

@Data
public class TimerRegisterResultDTO {
    private TimerRegisterResultType resultType;
    private String message;

    public static TimerRegisterResultDTO success() {
        var timerRegisterResultDTO = new TimerRegisterResultDTO();
        timerRegisterResultDTO.setResultType(TimerRegisterResultType.SUCCESS);
        return timerRegisterResultDTO;
    }

    public static TimerRegisterResultDTO fail(String message) {
        var timerRegisterResultDTO = new TimerRegisterResultDTO();
        timerRegisterResultDTO.setResultType(TimerRegisterResultType.FAIL);
        timerRegisterResultDTO.setMessage(message);
        return timerRegisterResultDTO;
    }

    public static TimerRegisterResultDTO cooldown() {
        var timerRegisterResultDTO = new TimerRegisterResultDTO();
        timerRegisterResultDTO.setResultType(TimerRegisterResultType.COOLDOWN);
        return timerRegisterResultDTO;
    }
}
