package com.johnchoi96.webservice.models.uptime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UptimeResponse {

    @JsonProperty(value = "start_time")
    private String startTime;

    @JsonProperty(value = "time_zone")
    private String timeZone;

    private UptimeObject uptime;
}