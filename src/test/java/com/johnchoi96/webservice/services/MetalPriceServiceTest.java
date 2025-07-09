package com.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.johnchoi96.webservice.clients.MetalPriceClient;
import com.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import com.johnchoi96.webservice.models.metalprice.Rates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MetalPriceServiceTest {

    @Mock
    @Autowired
    private MetalPriceClient metalPriceClient;

    @Mock
    @Autowired
    private FCMService fcmService;

    @InjectMocks
    @Autowired
    private MetalPriceService metalPriceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnalyzeGoldPriceAndReportValidDay() throws JsonProcessingException, FirebaseMessagingException {
        final LocalDate date = LocalDate.of(2023, 9, 14);
        doReturn(getDummyRate(3.45)).when(metalPriceClient).getGoldRateForDate(any());
        doReturn(getDummyRate(2.34)).when(metalPriceClient).getLatestGoldRate();
        doNothing().when(fcmService).sendNotification(any(), any(), any(), any(), anyBoolean(), anyBoolean());

        metalPriceService.analyzeGoldPriceAndNotify(date);
        verify(metalPriceClient, times(1)).getGoldRateForDate(any());
    }

    @Test
    void testAnalyzeGoldPriceAndReportInvalidDay() throws JsonProcessingException, FirebaseMessagingException {
        final LocalDate date = LocalDate.of(2023, 9, 17);
        doReturn(getDummyRate(3.45)).when(metalPriceClient).getGoldRateForDate(any());
        doReturn(getDummyRate(2.34)).when(metalPriceClient).getLatestGoldRate();
        doNothing().when(fcmService).sendNotification(any(), any(), any(), any(), anyBoolean(), anyBoolean());

        metalPriceService.analyzeGoldPriceAndNotify(date);
        verify(metalPriceClient, times(0)).getGoldRateForDate(any());
    }

    private MetalPriceResponse getDummyRate(final double usdRate) {
        final MetalPriceResponse response = new MetalPriceResponse();
        final Rates rate = new Rates();
        rate.setUsd(usdRate);
        response.setRates(rate);
        return response;
    }
}
