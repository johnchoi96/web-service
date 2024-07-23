package io.github.johnchoi96.webservice.services;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class EmailServiceTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private SendGridApiProperties sendGridApiProperties;

    @InjectMocks
    @Autowired
    private EmailService emailService;

    private final String DUMMY_APP_ID = "dummy-app-id";

    private final String API_KEY = "API_KEY";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        doReturn(API_KEY).when(sendGridApiProperties).getApiKey();
        final Field field = EmailService.class.getDeclaredField("allowedAppIds");
        field.setAccessible(true);
        field.set(emailService, List.of(DUMMY_APP_ID));
    }

    @Test
    void testSendEmail() throws IOException, NoSuchFieldException, IllegalAccessException {
        var mockedSendGrid = mock(SendGrid.class);
        var mockedSendGridApiProp = mock(SendGridApiProperties.class);
        var mockedEnvironment = mock(Environment.class);
        var mockedActiveProfiles = new String[]{"local"};
        doReturn(mockedActiveProfiles).when(mockedEnvironment).getActiveProfiles();
        doReturn(API_KEY).when(mockedSendGridApiProp).getApiKey();
        EmailService service = spy(new EmailService(mockedSendGridApiProp, mockedEnvironment));
        doReturn(mockedSendGrid).when(service).makeSendGrid(API_KEY);
        doReturn(getDummyResponse(200)).when(mockedSendGrid).api(any(Request.class));

        Field field = EmailService.class.getDeclaredField("allowedAppIds");
        field.setAccessible(true);
        field.set(service, List.of(DUMMY_APP_ID));
        field = EmailService.class.getDeclaredField("sendGridApiProperties");
        field.setAccessible(true);
        field.set(service, mockedSendGridApiProp);
        assertTrue(service.sendEmailForContactMe(getCompleteRequest(), DUMMY_APP_ID));
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
        return new EmailRequest("TEST_SUBJECT", "TEST_BODY", "TEST_CONTACT_INFO");
    }
}
