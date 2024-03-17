package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.MetalPriceClient;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetalPriceService {

    private final MetalPriceClient metalPriceClient;

    private final FCMService fcmService;

    public void analyzeGoldPriceAndNotify(final LocalDate today) throws JsonProcessingException, FirebaseMessagingException {
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
