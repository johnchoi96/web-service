package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.clients.MetalPriceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetalPriceService {

    private final MetalPriceClient metalPriceClient;

    private final EmailService emailService;

    public void analyzeGoldPriceAndReport(final LocalDate today) throws JsonProcessingException {
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
            // send email
            log.info("Today's rate is lower so triggering an email");
            emailService.sendEmailForMetalPrice(previousDate, today, previousRate, todayRate);
        }
    }
}
