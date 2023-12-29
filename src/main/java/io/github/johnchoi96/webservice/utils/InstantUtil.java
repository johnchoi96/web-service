package io.github.johnchoi96.webservice.utils;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@UtilityClass
public class InstantUtil {

    public record DateObj(int year, int month, int day) {
    }

    public final String TIMEZONE_US_EAST = "America/New_York";

    public int getDifferenceInDays(final Instant first, final Instant second) {
        final Duration duration = Duration.between(first, second);
        final long daysDifference = duration.toDays();
        return Math.abs((int) daysDifference);
    }

    public DateObj getDateObject(final Instant instant) {
        // Convert Instant to ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(TIMEZONE_US_EAST));

        // Extract Year, Month, and Day
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonth().getValue();
        int day = zonedDateTime.getDayOfMonth();
        return new DateObj(year, month, day);
    }
}
