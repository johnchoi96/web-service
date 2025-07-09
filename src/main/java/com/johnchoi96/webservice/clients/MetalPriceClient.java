package com.johnchoi96.webservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import com.johnchoi96.webservice.properties.api.MetalPriceApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class MetalPriceClient {

    private final MetalPriceApiProperties metalPriceApiProperties;

    public MetalPriceResponse getGoldRateForDate(final LocalDate date) throws JsonProcessingException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return getGoldRate(formatter.format(date));
    }

    public MetalPriceResponse getLatestGoldRate() throws JsonProcessingException {
        return getGoldRate("latest");
    }

    private MetalPriceResponse getGoldRate(final String endpoint) throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final String result = restTemplate.getForObject(
                String.format(
                        "https://api.metalpriceapi.com/v1/%s?api_key=%s&base=XAU",
                        endpoint,
                        metalPriceApiProperties.getApiKey()
                ),
                String.class
        );
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, MetalPriceResponse.class);
    }
}
