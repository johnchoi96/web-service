package io.github.johnchoi96.webservice.models.metalprice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rates {
    @JsonProperty(value = "KRW")
    private Double krw;

    @JsonProperty(value = "USD")
    private Double usd;
}