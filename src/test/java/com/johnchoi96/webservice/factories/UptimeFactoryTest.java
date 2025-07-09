package com.johnchoi96.webservice.factories;

import com.johnchoi96.webservice.models.uptime.UptimeObject;
import com.johnchoi96.webservice.models.uptime.UptimeResponse;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UptimeFactoryTest {

    @Test
    void testBuild() {
        final UptimeResponse actual = UptimeFactory.build(getInstant(), getDuration());
        assertEquals("America/New_York", actual.getTimeZone());
    }

    @Test
    void testBuildUptime() {
        final UptimeObject actual = UptimeFactory.buildUptime(getDuration());
        assertEquals(0, actual.getDays());
        assertEquals(0, actual.getHours());
        assertEquals(0, actual.getSeconds());
        assertEquals(0, actual.getMinutes());
    }

    private Instant getInstant() {
        return Instant.now();
    }

    private Duration getDuration() {
        return Duration.ZERO;
    }
}
