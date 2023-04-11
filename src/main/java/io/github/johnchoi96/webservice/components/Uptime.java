package io.github.johnchoi96.webservice.components;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Getter
public class Uptime {

    private final Instant startTime = Instant.now();

    public String getUptime() {
        var currentTime = Instant.now();
        Duration duration = Duration.between(startTime, currentTime);
        return String.format("%d days, %d hours, %d minutes, and %d seconds",
                duration.toDaysPart(), duration.toHoursPart(),
                duration.toMinutesPart(), duration.toSecondsPart());
    }
}
