package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.uptime.UptimeObject;
import io.github.johnchoi96.webservice.models.uptime.UptimeResponse;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@UtilityClass
public class UptimeFactory {

    public UptimeResponse build(final Instant start, final Duration duration) {
        final String TIME_ZONE = "America/New_York";
        final ZonedDateTime zdt = start.atZone(ZoneId.of(TIME_ZONE));
        return UptimeResponse
                .builder()
                .startTime(zdt.toString())
                .timeZone(TIME_ZONE)
                .uptime(buildUptime(duration))
                .build();
    }

    public UptimeObject buildUptime(final Duration duration) {
        return UptimeObject.builder()
                .days((int) duration.toDaysPart())
                .hours(duration.toHoursPart())
                .minutes(duration.toMinutesPart())
                .seconds(duration.toSecondsPart())
                .build();
    }
}
