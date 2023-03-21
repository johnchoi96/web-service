package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class EmailBodyFactory {

    public String build(final EmailRequest emailRequest) {
        return String.format("Subject: %s\n\nBody:\n%s\n\nContact Info: %s\n",
                emailRequest.getSubject(),
                emailRequest.getBody(),
                Optional.ofNullable(emailRequest.getContactInfo()).orElse("Not Provided"));
    }
}
