package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.MetalPriceClient;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetalPriceService {

    private final MetalPriceClient metalPriceClient;

    private final EmailService emailService;

    private final FCMService fcmService;

    public void analyzeGoldPriceAndNotify(final LocalDate today) throws JsonProcessingException, FirebaseMessagingException {
        // compare XAU price with previous day's price
        compareGoldPriceWithPreviousDay(today);
    }

    private void compareGoldPriceWithPreviousDay(final LocalDate today) throws FirebaseMessagingException, JsonProcessingException {
        // get today's day of the week
        var day = today.getDayOfWeek();

        var previousDate = switch (day) {
            case TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> today.minusDays(1);
            case MONDAY -> today.minusDays(3);
            default -> null;
        };
        if (previousDate == null) {
            log.info("Today is not a valid day");
            return;
        }
        // get previous date's gold rate
        var previousRate = metalPriceClient.getGoldRateForDate(previousDate);
        // get today's rate
        var todayRate = metalPriceClient.getLatestGoldRate();
        notifyIfXAUPriceDecreased(previousRate, todayRate, previousDate, today);
        notifyIfPriceDroppedBelowTarget(List.of(1000.00, 2000.00), previousRate, todayRate, previousDate, today);
    }

    /**
     * TODO: Clean this up
     *
     * @param targetPrices should be sorted in non-decreasing order to ensure O(n).
     *                     Otherwise, this method would take O(n log n) to sort.
     * @param previousRate
     * @param todayRate
     * @param previousDate
     * @param today
     */
    private void notifyIfPriceDroppedBelowTarget(
            final List<Double> targetPrices,
            final MetalPriceResponse previousRate,
            final MetalPriceResponse todayRate,
            final LocalDate previousDate,
            final LocalDate today) throws FirebaseMessagingException {
        boolean isSorted = isSorted(targetPrices);
        if (!isSorted) {
            Collections.sort(targetPrices);
        }
        // targets: 1000, 2000, 3000, 4000
        // prev: 3100, today: 1500
        // find today. If between, get the next largest one so that we can say the price dropped below 2000
        // only if prev was higher than nextLargestTarget
        var tRate = todayRate.getRates().getUsd();
        var pRate = previousRate.getRates().getUsd();
        int left = 0, right = targetPrices.size();
        double nextLargestTarget = -1;
        while (left < right) {
            final int midpoint = left + (right - left) / 2;
            final double priceAtMid = targetPrices.get(midpoint);
            if (compareWithMargin(priceAtMid, tRate)) {
                nextLargestTarget = priceAtMid;
                break;
            }
            // check if priceAtMid is between the one before and one after
            // if between, return next largest in the target list
            if (midpoint != 0 && midpoint != targetPrices.size() - 1) {
                if (priceAtMid > targetPrices.get(midpoint - 1) && priceAtMid < targetPrices.get(midpoint + 1)) {
                    nextLargestTarget = targetPrices.get(midpoint + 1);
                    break;
                }
            }

            if (priceAtMid > tRate) {
                // slide window to left
                right = midpoint;
            } else {
                // slide window to right
                left = midpoint + 1;
            }
        }
        // if nextLargestTarget is still -1, check with first and last value in targetprices
        if (compareWithMargin(nextLargestTarget, -1)) {
            if (tRate < targetPrices.get(0)) {
                nextLargestTarget = targetPrices.get(0);
            } else {
                // do nothing
                return;
            }
        }
        if (pRate > nextLargestTarget) {
            // trigger notification
            log.info("Today's rate is lower than set target so triggering a notification");
            // send notification
            final String notificationTitle = "Message from Web Service for MetalPrice";
            final String notificationBody = String.format("Gold Rate below %.2f!", nextLargestTarget);
            final StringBuilder metalpriceBody = FCMBodyFactory.buildBodyForMetalPrice(previousDate, today, previousRate, todayRate);
            fcmService.sendNotification(FCMTopic.METALPRICE, notificationTitle, notificationBody, metalpriceBody, true, false);
        }
    }

    private boolean compareWithMargin(double a, double b) {
        return Math.abs(a - b) <= 0.01;
    }

    private boolean isSorted(final List<Double> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) > list.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void notifyIfXAUPriceDecreased(
            final MetalPriceResponse previousRate,
            final MetalPriceResponse todayRate,
            final LocalDate previousDate,
            final LocalDate today) throws FirebaseMessagingException {
        // if today's rate is lower than the previous rate, send email
        log.info(String.format("Prev rate: $%.2f, today's rate: $%.2f",
                previousRate.getRates().getUsd(), todayRate.getRates().getUsd()));
        if (todayRate.getRates().getUsd() < previousRate.getRates().getUsd()) {
            // send email
            log.info("Today's rate is lower so triggering an email");
            emailService.sendEmailForMetalPrice(previousDate, today, previousRate, todayRate);
            // send notification
            final String notificationTitle = "Message from Web Service for MetalPrice";
            final String notificationBody = "Tap to see today's Gold Rate!";
            final StringBuilder metalpriceBody = FCMBodyFactory.buildBodyForMetalPrice(previousDate, today, previousRate, todayRate);
            fcmService.sendNotification(FCMTopic.METALPRICE, notificationTitle, notificationBody, metalpriceBody, true, false);
        }
    }
}
