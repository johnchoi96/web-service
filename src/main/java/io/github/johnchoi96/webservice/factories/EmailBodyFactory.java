package io.github.johnchoi96.webservice.factories;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailBodyFactory {

    public String build(final String subject, final String body) {
        return String.format("Subject: %s\n\nBody:\n%s\n", subject, body);
    }
}
