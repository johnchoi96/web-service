package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.MetalPriceClient;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import io.github.johnchoi96.webservice.utils.MetalPriceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetalPriceService {

    private final MetalPriceClient metalPriceClient;

    private final FCMService fcmService;

    public void analyzeGoldPriceAndNotify(final LocalDate today) throws JsonProcessingException, FirebaseMessagingException {
        // compare XAU price with previous day's price
        compareGoldPriceWithPreviousDay(today);
    }

    /**
     * Checks if XAU rate dropped compared to previous day's rate.
     * Checks if XAU rate dropped below target rate.
     *
     * @param today today's date
     * @throws FirebaseMessagingException if notification failed to send
     * @throws JsonProcessingException    if API call failed
     */
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

    private void notifyIfPriceDroppedBelowTarget(
            final List<Double> targetPrices,
            final MetalPriceResponse previousRate,
            final MetalPriceResponse todayRate,
            final LocalDate previousDate,
            final LocalDate today) throws FirebaseMessagingException {
        final Double closestTarget = MetalPriceUtils.priceDidDropBelowTarget(targetPrices, previousRate.getRates().getUsd(), todayRate.getRates().getUsd());
        if (closestTarget != null) {
            // trigger notification
            log.info("Today's rate is lower than set target so triggering a notification");
            // send notification
            final String notificationTitle = "Message from Web Service for MetalPrice";
            final String notificationBody = String.format("Gold Rate below %.2f!", closestTarget);
            final StringBuilder metalpriceBody = FCMBodyFactory.buildBodyForMetalPrice(previousDate, today, previousRate, todayRate);
            fcmService.sendNotification(FCMTopic.METALPRICE, notificationTitle, notificationBody, metalpriceBody, true, false);
        }
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
            // send notification
            log.info("Today's rate is lower so triggering a notification");
            final String notificationTitle = "Message from Web Service for MetalPrice";
            final String notificationBody = "Tap to see today's Gold Rate!";
            final StringBuilder metalpriceBody = FCMBodyFactory.buildBodyForMetalPrice(previousDate, today, previousRate, todayRate);
            fcmService.sendNotification(FCMTopic.METALPRICE, notificationTitle, notificationBody, metalpriceBody, true, false);
        }
    }
}
