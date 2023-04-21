package io.github.johnchoi96.webservice.services;

import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class EmailServiceTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private SendGridApiProperties sendGridApiProperties;

    @InjectMocks
    @Autowired
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doReturn("API_KEY").when(sendGridApiProperties).getApiKey();
    }

    @Test
    @Disabled
    void testSendEmail() throws IOException {
        doReturn(getDummyResponse(200)).when(sendGrid).api(any());
        assertTrue(emailService.sendEmailForContactMe(getCompleteRequest(), "johnchoi-portfolio"));
    }

    @Test
    void testSendEmailWithBadResponse() throws IOException {
        doReturn(getDummyResponse(400)).when(sendGrid).api(any());
        assertFalse(emailService.sendEmailForContactMe(getCompleteRequest(), "johnchoi-portfolio"));
    }

    private Response getDummyResponse(final int statusCode) {
        final Response response = new Response();
        response.setStatusCode(statusCode);
        return response;
    }

    private EmailRequest getCompleteRequest() {
        final EmailRequest request = new EmailRequest();
        request.setSubject("TEST_SUBJECT");
        request.setBody("TEST_BODY");
        request.setContactInfo("TEST_CONTACT_INFO");
        return request;
    }
}
