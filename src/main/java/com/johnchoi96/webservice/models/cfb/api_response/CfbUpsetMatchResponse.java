package com.johnchoi96.webservice.models.cfb.api_response;

import com.johnchoi96.webservice.entities.cfb.CfbUpsetEntity;
import com.johnchoi96.webservice.entities.cfb.CfbWeekSummaryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CfbUpsetMatchResponse {
    private List<CfbUpsetEntity> upsetGames;

    private CfbWeekSummaryEntity weekSummary;
}
