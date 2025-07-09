package com.johnchoi96.webservice.models.cfb.win_probability;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WinProbabilityResponseItem {
    private int gameId;

    private String seasonType;

    private int week;

    private String awayTeam;

    @JsonProperty(value = "season")
    private int seasonYear;

    private String homeTeam;

    @JsonProperty(value = "homeWinProbability")
    private Float homeWinProb;

    private Float spread;
}