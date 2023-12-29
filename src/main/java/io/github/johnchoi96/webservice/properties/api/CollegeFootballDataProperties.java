package io.github.johnchoi96.webservice.properties.api;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "college-football-data")
@Slf4j
public class CollegeFootballDataProperties {

    private String apiKey;
}
