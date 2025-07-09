package com.johnchoi96.webservice.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetalPriceUtilsTest {

    @Test
    void testPriceDidDropBelowTarget_EmptyList() {
        final List<Double> inputList = List.of();
        final double prev = 3100.0;
        final double today = 1500.0;
        final Double actual = MetalPriceUtils.priceDidDropBelowTarget(inputList, prev, today);
        assertNull(actual);
    }

    @Test
    void testPriceDidDropBelowTarget_TargetListWithValueBetween() {
        final List<Double> inputList = List.of(
                1000.0,
                2000.0,
                3000.0,
                4000.0
        );
        final double prev = 3100.0;
        final double today = 1500.0;
        final Double actual = MetalPriceUtils.priceDidDropBelowTarget(inputList, prev, today);
        final double expected = 2000.0;
        assertTrue(MetalPriceUtils.compareWithMargin(actual, expected));
    }

    @Test
    void testPriceDidDropBelowTarget_leftBound() {
        final List<Double> inputList = List.of(
                1000.0,
                2000.0,
                3000.0,
                4000.0
        );
        final double prev = 3100.0;
        final double today = 900.0;
        final Double actual = MetalPriceUtils.priceDidDropBelowTarget(inputList, prev, today);
        final double expected = 1000.0;
        assertTrue(MetalPriceUtils.compareWithMargin(actual, expected));
    }

    @Test
    void testPriceDidDropBelowTarget_rightBound() {
        final List<Double> inputList = List.of(
                1000.0,
                2000.0,
                3000.0,
                4000.0
        );
        final double prev = 3100.0;
        final double today = 5000.0;
        final Double actual = MetalPriceUtils.priceDidDropBelowTarget(inputList, prev, today);
        assertNull(actual);
    }

    @Test
    void testPriceDidDropBelowTarget_exactValue() {
        final List<Double> inputList = List.of(
                1000.0,
                2000.0,
                3000.0,
                4000.0
        );
        final double prev = 3100.0;
        final double today = 2000.0;
        final Double actual = MetalPriceUtils.priceDidDropBelowTarget(inputList, prev, today);
        final double expected = 2000.0;
        assertTrue(MetalPriceUtils.compareWithMargin(actual, expected));
    }

    @Test
    void testCompareWithMargin_0() {
        final double first = 1000.0;
        final double second = 1000.0001;
        assertTrue(MetalPriceUtils.compareWithMargin(first, second));
    }

    @Test
    void testCompareWithMargin_1() {
        final double first = 1000.0;
        final double second = 1000.0;
        assertTrue(MetalPriceUtils.compareWithMargin(first, second));
    }

    @Test
    void testCompareWithMargin_2() {
        final double first = 1000.0;
        final double second = 1000.1;
        assertFalse(MetalPriceUtils.compareWithMargin(first, second));
    }

    @Test
    void testCompareWithMargin_3() {
        final double first = 1000.00;
        final double second = 1000.01;
        assertFalse(MetalPriceUtils.compareWithMargin(first, second));
    }

    @Test
    void testCompareWithMargin_4() {
        final double first = 2000.00;
        final double second = 1000.00;
        assertFalse(MetalPriceUtils.compareWithMargin(first, second));
    }

    @Test
    void testCompareWithMargin_5() {
        final double first = 1000.00;
        final double second = 1000.00;
        assertTrue(MetalPriceUtils.compareWithMargin(first, second));
    }
}
