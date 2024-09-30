package io.github.johnchoi96.webservice.models.cfb.api_response;

import io.github.johnchoi96.webservice.entities.cfb.CfbUpsetEntity;
import io.github.johnchoi96.webservice.entities.cfb.CfbWeekSummaryEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CfbUpsetMatchResponse {
    private List<CfbUpsetEntity> upsetGames;

    private CfbWeekSummaryEntity weekSummary;
}
