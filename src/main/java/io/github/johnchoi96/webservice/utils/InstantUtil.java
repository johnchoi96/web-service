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

    public record DateTimeObj(int year, int month, int day, int hour,
                              int minute, int second) {
    }

    public final String TIMEZONE_US_CENTRAL = "America/Chicago";

    public int getDifferenceInDays(final Instant first, final Instant second) {
        final Duration duration = Duration.between(first, second);
        final long daysDifference = duration.toDays();
        return Math.abs((int) daysDifference);
    }

    public DateObj getDateObject(final Instant instant) {
        // Convert Instant to ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(TIMEZONE_US_CENTRAL));

        // Extract Year, Month, and Day
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonth().getValue();
        int day = zonedDateTime.getDayOfMonth();
        return new DateObj(year, month, day);
    }

    public DateTimeObj getDateTimeObject(final Instant instant) {
        // Convert Instant to ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(TIMEZONE_US_CENTRAL));

        // Extract Year, Month, and Day
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonth().getValue();
        int day = zonedDateTime.getDayOfMonth();
        int hour = zonedDateTime.getHour();
        int minute = zonedDateTime.getMinute();
        int second = zonedDateTime.getSecond();
        return new DateTimeObj(year, month, day, hour, minute, second);
    }
}
