package io.github.johnchoi96.webservice.models.cfb.win_probability;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WinProbabilityResponseItem {
    private int gameId;

    private String seasonType;

    private int week;

    private String awayTeam;

    @JsonProperty(value = "season")
    private int seasonYear;

    private String homeTeam;

    private Float homeWinProb;

    private Float spread;
}