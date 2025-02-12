package io.github.johnchoi96.webservice.models.ellie;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ElliePayload {

    private String greetingMessage;

    private String yesMessage;

    private String noMessage;

    private String helloKorean;

    private String offerAcceptedTitle;

    private String offerAcceptedBody;

    private String offerAcceptedTooltip;

    private String authenticationTooltip;

    private String modalMessage;

    private String modalTitle;
}
