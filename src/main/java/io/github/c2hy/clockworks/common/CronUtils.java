package io.github.c2hy.clockworks.common;


import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Slf4j
public class CronUtils {
    private static final CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING53);
    private static final CronParser cronParser = new CronParser(cronDefinition);

    public static boolean isInvalidExpression(String expression) {
        try {
            cronParser.parse(expression).validate();
        } catch (Exception e) {
            log.warn("valid cron expression {}", expression);
            return true;
        }

        return false;
    }

    @Nonnull
    public static OffsetDateTime next(String expression) {
        return ExecutionTime.forCron(cronParser.parse(expression))
                .nextExecution(OffsetDateTime.now().toZonedDateTime())
                .map(ZonedDateTime::toOffsetDateTime)
                .orElseThrow();
    }
}
