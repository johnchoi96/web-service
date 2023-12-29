package io.github.johnchoi96.webservice.models.cfb.rankings;

import lombok.Getter;

import java.util.List;

@Getter
public class PollsItem {
    private List<RanksItem> ranks;

    private String poll;
}