package io.github.johnchoi96.webservice.models.petfinder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Colors {
    private String secondary;

    private String tertiary;

    private String primary;
}