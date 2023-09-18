package io.github.johnchoi96.webservice.models.metalprice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetalPriceResponse {
    private boolean success;

    private Rates rates;

    private String base;

    private long timestamp;
}