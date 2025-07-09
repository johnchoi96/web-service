package com.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Breeds {
    private String secondary;

    private Boolean mixed;

    private String primary;

    private Boolean unknown;
}