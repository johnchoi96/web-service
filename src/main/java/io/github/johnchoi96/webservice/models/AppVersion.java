package io.github.johnchoi96.webservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AppVersion {

    private String version;
}
