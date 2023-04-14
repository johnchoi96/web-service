package io.github.johnchoi96.webservice.models.petfinder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {
    private boolean shotsCurrent;

    private boolean specialNeeds;

    private Object declawed;

    private boolean spayedNeutered;

    private boolean houseTrained;
}