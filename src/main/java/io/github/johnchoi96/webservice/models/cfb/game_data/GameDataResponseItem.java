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

    private Float awayPostWinProb;

    private String notes;

    private boolean startTimeTbd;

    private String awayTeam;

    private int awayId;

    private Float homePostWinProb;

    private String seasonType;

    private int homePoints;

    private int awayPregameElo;

    private String homeConference;

    private int awayPoints;

    private int homePregameElo;

    @JsonProperty(value = "season")
    private int seasonYear;

    private int awayPostgameElo;

    private int homeId;

    private int homePostgameElo;

    private int id;

    private String awayConference;

    private int venueId;

    @JsonDeserialize(using = StringInstantDeserializer.class)
    private Instant startDate;

    private boolean neutralSite;

    private boolean conferenceGame;

    private String awayDivision;

    private Float excitementIndex;

    private boolean completed;

    private List<Integer> awayLineScores;

    private String homeDivision;

    private List<Integer> homeLineScores;

    private String highlights;

    private String homeTeam;

    private int attendance;
}