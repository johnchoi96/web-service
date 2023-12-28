package io.github.johnchoi96.webservice.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstantUtilTest {

    @Test
    void testInstantDifferenceInDays() {
        // Create two Instant objects
        Instant instant1 = Instant.parse("2023-11-29T04:27:39.099159Z");
        Instant instant2 = Instant.parse("2023-12-05T10:15:30.500000Z");

        final int expectedDays = 6;
        int actual = InstantUtil.getDifferenceInDays(instant1, instant2);
        assertEquals(expectedDays, actual);

        actual = InstantUtil.getDifferenceInDays(instant2, instant1);
        assertEquals(expectedDays, actual);
    }

    @Test
    void testInstantDifferenceByOneDay() {
        // Create two Instant objects
        Instant instant1 = Instant.now();
        final long dayInSeconds = 60 * 60 * 24;
        Instant instant2 = instant1.plusSeconds(dayInSeconds);

        final int expectedDays = 1;
        int actual = InstantUtil.getDifferenceInDays(instant1, instant2);
        assertEquals(expectedDays, actual);

        actual = InstantUtil.getDifferenceInDays(instant2, instant1);
        assertEquals(expectedDays, actual);
    }

    @Test
    void testInstantLessThanDay() {
        // Create two Instant objects
        Instant instant1 = Instant.now();
        final long offset = 60 * 60 * 2; // 2 hours
        Instant instant2 = instant1.plusSeconds(offset);

        final int expectedDays = 0;
        int actual = InstantUtil.getDifferenceInDays(instant1, instant2);
        assertEquals(expectedDays, actual);

        actual = InstantUtil.getDifferenceInDays(instant2, instant1);
        assertEquals(expectedDays, actual);
    }
}
