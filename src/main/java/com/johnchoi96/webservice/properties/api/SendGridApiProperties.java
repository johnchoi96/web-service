package com.johnchoi96.webservice.properties.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "sendgrid")
public class SendGridApiProperties {

    private String apiKey;
}
