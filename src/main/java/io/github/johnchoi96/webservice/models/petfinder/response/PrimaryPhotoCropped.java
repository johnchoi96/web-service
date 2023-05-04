package io.github.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryPhotoCropped {
    private String small;

    private String large;

    private String medium;

    private String full;
}