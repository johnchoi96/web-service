package io.github.johnchoi96.webservice.models.cfb.game_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.johnchoi96.webservice.json.StringInstantDeserializer;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class GameDataResponseItem {
    private String venue;

    private int week;

    private String notes;

    private String awayTeam;

    private boolean startTimeTBD;

    private String homeClassification;

    private int homePostgameElo;

    private boolean conferenceGame;

    private int homePostgameWinProbability;

    private int awayPregameElo;

    private int venueId;

    @JsonProperty(value = "season")
    private int seasonYear;

    private String homeTeam;

    private int id;

    private List<Integer> awayLineScores;

    private boolean neutralSite;

    private int homeId;

    private String awayConference;

    private String seasonType;

    private String homeConference;

    private int awayId;

    private int awayPoints;

    private int homePoints;

    private boolean completed;

    private int awayPostgameElo;

    private int homePregameElo;

    private List<Integer> homeLineScores;

    private String highlights;

    private int excitementIndex;

    private String awayClassification;

    private int awayPostgameWinProbability;

    @JsonDeserialize(using = StringInstantDeserializer.class)
    private Instant startDate;

    private int attendance;
}