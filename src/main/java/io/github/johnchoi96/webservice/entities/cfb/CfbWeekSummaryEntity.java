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
@Table(name = "WEBSERVICE_CFB_WEEK_SUMMARY")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CfbWeekSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "WEEK", nullable = false)
    private Integer week;

    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @Column(name = "UPSET_MATCH_COUNT", nullable = false)
    private Integer upsetMatchCount;

    @Column(name = "TOTAL_MATCH_COUNT", nullable = false)
    private Integer totalMatchCount;

    @Column(name = "RANK_UPSET_COUNT", nullable = false)
    private Integer rankUpsetCount;

    @Column(name = "PREDICTION_UPSET_COUNT", nullable = false)
    private Integer predictionUpsetCount;

    @ManyToOne
    @JoinColumn(name = "SEASON_TYPE", nullable = false)
    private CfbSeasonTypeEntity seasonType;

    @Column(name = "START_TIMESTAMP", nullable = false)
    private Instant start;

    @Column(name = "END_TIMESTAMP", nullable = false)
    private Instant end;

    @Column(name = "FINALIZED", nullable = false)
    private boolean isFinalized = false;
}
