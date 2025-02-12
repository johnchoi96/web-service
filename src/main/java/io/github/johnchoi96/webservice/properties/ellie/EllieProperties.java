package io.github.johnchoi96.webservice.properties.ellie;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ellie")
public class EllieProperties {

    private String password;

    private String greetingMessage;

    private String yesMessage;

    private String noMessage;

    private String helloKorean;

    private String offerAcceptedTitle;

    private String offerAcceptedBody;

    private String offerAcceptedTooltip;

    private String authenticationTooltip;
}