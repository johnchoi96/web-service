package io.github.johnchoi96.webservice.models.cfb.rankings;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingResponseItem {
    private String seasonType;

    private int week;

    private int season;

    private List<PollsItem> polls;
}