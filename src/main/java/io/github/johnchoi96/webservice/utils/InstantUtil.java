package io.github.johnchoi96.webservice.utils;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;

@UtilityClass
public class InstantUtil {

    public int getDifferenceInDays(final Instant first, final Instant second) {
        final Duration duration = Duration.between(first, second);
        final long daysDifference = duration.toDays();
        return Math.abs((int) daysDifference);
    }
}
