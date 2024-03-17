package io.github.johnchoi96.webservice.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.github.johnchoi96.webservice.factories.EmailBodyFactory;
import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final SendGridApiProperties sendGridApiProperties;

    private final WebAppMetadataProperties webAppMetadataProperties;

    private final Environment environment;

    private Instant latestTimestamp;

    private final int TIME_LIMIT_HOURS = 1;

    private final int REQUEST_LIMIT = 10;

    private int numOfRequests;

    private List<String> allowedAppIds;

    @PostConstruct
    private void init() {
        allowedAppIds = Stream.of(webAppMetadataProperties.getAppId())
                .map(String::toLowerCase).toList();
    }

    private boolean requestAllowed(final String appId) {
        if (!allowedAppIds.contains(appId.toLowerCase())) {
            return false;
        }
        if (latestTimestamp == null) {
            latestTimestamp = Instant.now();
            numOfRequests = 0;
        } else {
            // check if timestamp was more than an TIME_LIMIT_HOURS ago
            var duration = Duration.between(latestTimestamp, Instant.now());
            if (duration.toHoursPart() < TIME_LIMIT_HOURS) {
                // check if numOfRequests does not exceed REQUEST_LIMIT
                if (numOfRequests >= REQUEST_LIMIT) {
                    return false;
                }
            } else {
                // if it has been more than TIME_LIMIT_HOURS, reset timestamp
                // and numOfRequests
                latestTimestamp = Instant.now();
                numOfRequests = 0;
            }
        }
        numOfRequests++;
        return true;
    }

    protected SendGrid makeSendGrid(final String apiKey) {
        return new SendGrid(apiKey);
    }

    public boolean sendEmailForContactMe(final EmailRequest request, final String appId) {
        if (!requestAllowed(appId)) {
            if (latestTimestamp == null) {
                log.info("Request was not allowed");
            } else {
                log.info("Number of requests exceeded. Next request available at {}",
                        latestTimestamp.plusSeconds(60 * 60)
                                .atZone(ZoneId.of("America/New_York")));
            }

            return false;
        }
        final String apiKey = sendGridApiProperties.getApiKey();
        final String EMAIL_ADDRESS = "johnchoi1003@icloud.com";

        final Email from = new Email(EMAIL_ADDRESS);
        final Email to = new Email(EMAIL_ADDRESS);
        final Content content = new Content("text/plain", EmailBodyFactory.buildBodyFromWebApp(request));
        final String EMAIL_SUBJECT = "Message from Web App";
        final Mail mail = new Mail(from, EMAIL_SUBJECT, to, content);

        final SendGrid sg = makeSendGrid(apiKey);
        final Request emailRequest = new Request();
        try {
            emailRequest.setMethod(Method.POST);
            emailRequest.setEndpoint("mail/send");
            emailRequest.setBody(mail.build());
            final Response response = sg.api(emailRequest);
            log.debug("Response status code: {}", response.getStatusCode());
            return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public void notifyException(final Exception exception) {
        // determine active profile. If local, don't do anything
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            log.info("Local profile activated. Email will not be sent.");
            return;
        }
        final String apiKey = sendGridApiProperties.getApiKey();
        final String EMAIL_ADDRESS = "johnchoi1003@icloud.com";

        final Email from = new Email(EMAIL_ADDRESS);
        final Email to = new Email(EMAIL_ADDRESS);
        final Content content = new Content("text/html", EmailBodyFactory.buildBodyForExceptionNotification(exception));
        final String EMAIL_SUBJECT = "Exception Notification for Web Service";
        final Mail mail = new Mail(from, EMAIL_SUBJECT, to, content);

        final SendGrid sg = new SendGrid(apiKey);
        final Request emailRequest = new Request();
        try {
            emailRequest.setMethod(Method.POST);
            emailRequest.setEndpoint("mail/send");
            emailRequest.setBody(mail.build());
            final Response response = sg.api(emailRequest);
            log.debug("Response status code: {}", response.getStatusCode());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
