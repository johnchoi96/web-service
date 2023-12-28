package io.github.johnchoi96.webservice.models.cfb;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UpsetGame {

    private Integer homeRank;

    private Integer awayRank;

    private Float preGameHomeWinProbability;

    private String homeTeamName;

    private String awayTeamName;

    private int homePoints;

    private int awayPoints;

    private int week;

    private int year;

    private String seasonType;

    private String location;

    private String bowlName;

    private Instant timestamp;

    private Boolean neutralSite;

    private Boolean conferenceGame;
}
