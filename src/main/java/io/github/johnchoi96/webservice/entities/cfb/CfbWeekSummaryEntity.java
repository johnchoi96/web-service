package io.github.johnchoi96.webservice.entities.cfb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WEBSERVICE_CFB_WEEK_SUMMARY")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CfbWeekSummaryEntity {

    @Id
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
}
