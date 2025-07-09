package com.johnchoi96.webservice.models.cfb.game_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.johnchoi96.webservice.json.StringInstantDeserializer;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class GameDataResponseItem {
    private String venue;

    private Integer week;

    private String notes;

    private String awayTeam;

    private Boolean startTimeTBD;

    private String homeClassification;

    private Integer homePostgameElo;

    @JsonProperty(value = "conferenceGame")
    private Boolean isConferenceGame;

    private Integer homePostgameWinProbability;

    private Integer awayPregameElo;

    private Integer venueId;

    @JsonProperty(value = "season")
    private Integer seasonYear;

    private String homeTeam;

    private Integer id;

    private List<Integer> awayLineScores;

    @JsonProperty(value = "neutralSite")
    private Boolean isNeutralSite;

    private Integer homeId;

    private String awayConference;

    private String seasonType;

    private String homeConference;

    private Integer awayId;

    private Integer awayPoints;

    private Integer homePoints;

    @JsonProperty(value = "completed")
    private Boolean isCompleted;

    private Integer awayPostgameElo;

    private Integer homePregameElo;

    private List<Integer> homeLineScores;

    private String highlights;

    private Integer excitementIndex;

    private String awayClassification;

    private Integer awayPostgameWinProbability;

    @JsonDeserialize(using = StringInstantDeserializer.class)
    private Instant startDate;

    private Integer attendance;
}