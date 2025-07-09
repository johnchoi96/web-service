package com.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links {
    private Organization organization;

    private Self self;

    private Type type;
}