package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.models.ellie.ElliePayload;
import io.github.johnchoi96.webservice.properties.ellie.EllieProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EllieService {

    private final EllieProperties ellieProperties;

    public boolean checkPassword(final String password) {
        return password.equals(ellieProperties.getPassword());
    }

    public ElliePayload getPayload() {
        return ElliePayload.builder()
                .greetingMessage(ellieProperties.getGreetingMessage())
                .yesMessage(ellieProperties.getYesMessage())
                .noMessage(ellieProperties.getNoMessage())
                .helloKorean(ellieProperties.getHelloKorean())
                .offerAcceptedTitle(ellieProperties.getOfferAcceptedTitle())
                .offerAcceptedBody(ellieProperties.getOfferAcceptedBody())
                .build();
    }
}
