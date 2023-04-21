package io.github.johnchoi96.webservice.services;

import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class EmailServiceTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private SendGridApiProperties sendGridApiProperties;

    @Mock
    private WebAppMetadataProperties webAppMetadataProperties;

    @InjectMocks
    @Autowired
    private EmailService emailService;

    private final String DUMMY_APP_ID = "dummy-app-id";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        doReturn("API_KEY").when(sendGridApiProperties).getApiKey();
        final Field field = EmailService.class.getDeclaredField("allowedAppIds");
        field.setAccessible(true);
        field.set(emailService, List.of(DUMMY_APP_ID));
    }

    @Test
    @Disabled
    void testSendEmail() throws IOException {
        doReturn(getDummyResponse(200)).when(sendGrid).api(any());
        assertTrue(emailService.sendEmailForContactMe(getCompleteRequest(), DUMMY_APP_ID));
    }

    @Test
    void testSendEmailWithBadResponse() throws IOException {
        doReturn(getDummyResponse(400)).when(sendGrid).api(any());
        assertFalse(emailService.sendEmailForContactMe(getCompleteRequest(), DUMMY_APP_ID));
    }

    @Test
    void testInvalidAppId() {
        assertFalse(emailService.sendEmailForContactMe(getCompleteRequest(), "invalid-app-id"));
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
