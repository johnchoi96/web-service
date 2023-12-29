package io.github.johnchoi96.webservice.models.cfb.rankings;

import lombok.Getter;

@Getter
public class RanksItem {
    private String conference;

    private int firstPlaceVotes;

    private String school;

    private int rank;

    private int points;
}