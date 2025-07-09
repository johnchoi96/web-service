package com.johnchoi96.webservice.models.cfb.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.johnchoi96.webservice.json.StringInstantDeserializer;
import lombok.Getter;

import java.time.Instant;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarResponseItem {
    @JsonDeserialize(using = StringInstantDeserializer.class)
    private Instant firstGameStart;

    private String seasonType;

    private int week;

    @JsonDeserialize(using = StringInstantDeserializer.class)
    private Instant lastGameStart;

    @JsonProperty(value = "season")
    private Integer seasonYear;
}