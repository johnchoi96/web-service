package io.github.johnchoi96.webservice.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class MetalPriceUtils {

    public boolean compareWithMargin(double a, double b) {
        return Math.abs(a - b) <= 0.001;
    }

    public boolean isSorted(final List<Double> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) > list.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Given previous rate and today rate with the target prices sorted, returns
     * the closest target price that is equal or larger than todayRate and
     * is less than previousRate. Returns null if not found.
     * i.e. if given:
     * targets: 1000, 2000, 3000, 4000,
     * prev: 3100,
     * today: 1500,
     * returns 2000 because 1500 is closest to 2000 and it's less than previous rate.
     *
     * @param targetPrices sorted list of target prices to guarantee O(n) runtime. O(n log n) if not sorted.
     * @param previousRate previous rate
     * @param todayRate    today's rate
     * @return closest target price if found, null if not found
     */
    public Double priceDidDropBelowTarget(@NonNull final List<Double> targetPrices,
                                          final double previousRate,
                                          final double todayRate) {
        boolean isSorted = MetalPriceUtils.isSorted(targetPrices);
        if (!isSorted) {
            Collections.sort(targetPrices);
        }
        // targets: 1000, 2000, 3000, 4000
        // prev: 3100, today: 1500
        // find today. If between, get the next largest one so that we can say the price dropped below 2000
        // only if prev was higher than nextLargestTarget
        int left = 0, right = targetPrices.size();
        double nextLargestTarget = -1;
        while (left < right) {
            final int midpoint = left + (right - left) / 2;
            final double priceAtMid = targetPrices.get(midpoint);
            if (compareWithMargin(priceAtMid, todayRate)) {
                nextLargestTarget = priceAtMid;
                break;
            }
            // check if priceAtMid is between the one before and one after
            // if between, return next largest in the target list
            if (midpoint != 0 && midpoint != targetPrices.size() - 1) {
                if (todayRate > targetPrices.get(midpoint - 1) && todayRate < priceAtMid) {
                    nextLargestTarget = priceAtMid;
                    break;
                }
            }

            if (priceAtMid > todayRate) {
                // slide window to left
                right = midpoint;
            } else {
                // slide window to right
                left = midpoint + 1;
            }
        }
        // if nextLargestTarget is still -1, check with first and last value in target prices
        if (compareWithMargin(nextLargestTarget, -1)) {
            if (!targetPrices.isEmpty() && todayRate < targetPrices.get(0)) {
                nextLargestTarget = targetPrices.get(0);
            } else {
                // do nothing
                return null;
            }
        }
        if (previousRate > nextLargestTarget) {
            return nextLargestTarget;
        }
        return null;
    }
}
