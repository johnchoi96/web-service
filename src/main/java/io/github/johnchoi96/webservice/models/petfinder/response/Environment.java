package io.github.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {
    private Boolean cats;

    private Boolean children;

    private Boolean dogs;
}