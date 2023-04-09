package io.github.johnchoi96.webservice.services;

import com.sendgrid.*;
import io.github.johnchoi96.webservice.configs.SendGridApiConfiguration;
import io.github.johnchoi96.webservice.factories.EmailBodyFactory;
import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.models.petfinder.PetfinderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private SendGridApiConfiguration sendGridApiConfiguration;

    public boolean sendEmail(final EmailRequest request, final String apiKey) {
        final String EMAIL_ADDRESS = "johnchoi1003@icloud.com";

        final Email from = new Email(EMAIL_ADDRESS);
        final Email to = new Email(EMAIL_ADDRESS);
        final Content content = new Content("text/plain", EmailBodyFactory.buildBodyFromWebApp(request));
        final String EMAIL_SUBJECT = "Message from Web App";
        final Mail mail = new Mail(from, EMAIL_SUBJECT, to, content);

        final SendGrid sg = new SendGrid(apiKey);
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

    public void sendEmailForPetfinder(final PetfinderResponse petfinderResponse) {
        final String apiKey = sendGridApiConfiguration.getApiKey();
        final String EMAIL_ADDRESS = "johnchoi1003@icloud.com";

        final Email from = new Email(EMAIL_ADDRESS);
        final Email to = new Email(EMAIL_ADDRESS);
        final Content content = new Content("text/plain", EmailBodyFactory.buildBodyForPetfinder(petfinderResponse));
        final String EMAIL_SUBJECT = "Message from Web Service For Petfinder";
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
