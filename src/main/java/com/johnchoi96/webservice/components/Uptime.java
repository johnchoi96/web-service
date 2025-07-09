package com.johnchoi96.webservice.components;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Getter
public class Uptime {

    private final Instant startTime = Instant.now();

    public Duration getUptime() {
        var currentTime = Instant.now();
        return Duration.between(startTime, currentTime);
    }
}
