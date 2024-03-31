package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.utils.InstantUtil;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class EmailBodyFactory {

    public String buildBodyFromWebApp(final EmailRequest emailRequest) {
        return String.format("Subject: %s\n\nBody:\n%s\n\nContact Info: %s\n",
                emailRequest.getSubject(),
                emailRequest.getBody(),
                Optional.ofNullable(emailRequest.getContactInfo()).orElse("Not Provided"));
    }

    public String buildBodyForExceptionNotification(final Exception exception) {
        final Instant now = Instant.now();
        final InstantUtil.DateTimeObj dateTimeObj = InstantUtil.getDateTimeObject(now);
        final String timestamp = String.format("%d/%d/%d at %d:%d:%d EST",
                dateTimeObj.month(), dateTimeObj.day(), dateTimeObj.year(),
                dateTimeObj.hour(), dateTimeObj.minute(), dateTimeObj.second());
        final String message = """
                <html><body>
                <h3>Web Service Exception Report</h3>
                <h6>Timestamp: %s</h6>
                <p>Exception message: %s</p>
                <p>Stacktrace:</p>
                <p>
                    %s
                </p>
                </body></html>
                """;
        return String.format(message, timestamp, exception.getLocalizedMessage(), Arrays.toString(exception.getStackTrace()));
    }
}
