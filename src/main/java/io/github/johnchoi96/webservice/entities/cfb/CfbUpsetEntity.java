package io.github.johnchoi96.webservice.entities.cfb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "WEBSERVICE_CFB_UPSET")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CfbUpsetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOCATION", length = 500)
    private String location;

    @Column(name = "BOWL_NAME", length = 250)
    private String bowlName;

    @ManyToOne
    @JoinColumn(name = "WINNING_TEAM", nullable = false)
    private CfbTeamEntity winningTeam;

    @ManyToOne
    @JoinColumn(name = "HOME_TEAM", nullable = false)
    private CfbTeamEntity homeTeam;

    @ManyToOne
    @JoinColumn(name = "AWAY_TEAM", nullable = false)
    private CfbTeamEntity awayTeam;

    @Column(name = "HOME_RANK")
    private Integer homeRank;

    @Column(name = "AWAY_RANK")
    private Integer awayRank;

    @Column(name = "PRE_MATCH_HOME_WIN_CHANCE", nullable = false)
    private Float preMatchHomeWinChance;

    @Column(name = "PRE_MATCH_AWAY_WIN_CHANCE", nullable = false)
    private Float preMatchAwayWinChance;

    @ManyToOne
    @JoinColumn(name = "UPSET_TYPE", nullable = false)
    private CfbUpsetTypeEntity upsetType;

    @Column(name = "HOME_SCORE", nullable = false)
    private Integer homeScore;

    @Column(name = "AWAY_SCORE", nullable = false)
    private Integer awayScore;

    @ManyToOne
    @JoinColumn(name = "CFB_WEEK", nullable = false)
    private CfbWeekSummaryEntity cfbWeek;

    @Column(name = "MATCH_TIMESTAMP", nullable = false)
    private Instant matchTimestamp;

    @Column(name = "NEUTRAL_SITE", nullable = false)
    private Boolean neutralSite;

    @Column(name = "CONFERENCE_GAME", nullable = false)
    private Boolean conferenceGame;
}
