package io.github.johnchoi96.webservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "sendgrid")
public class SendGridApiConfiguration {

    private String apiKey;
}
