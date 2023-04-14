package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.uptime.UptimeObject;
import io.github.johnchoi96.webservice.models.uptime.UptimeResponse;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UptimeFactoryTest {

    @Test
    void testBuild() {
        final UptimeResponse actual = UptimeFactory.build(getInstant(), getDuration());
        assertEquals(actual.getTimeZone(), "America/New_York");
    }

    @Test
    void testBuildUptime() {
        final UptimeObject actual = UptimeFactory.buildUptime(getDuration());
        assertEquals(actual.getDays(), 0);
        assertEquals(actual.getHours(), 0);
        assertEquals(actual.getSeconds(), 0);
        assertEquals(actual.getMinutes(), 0);
    }

    private Instant getInstant() {
        return Instant.now();
    }

    private Duration getDuration() {
        return Duration.ZERO;
    }
}
