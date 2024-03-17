package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailBodyFactoryTest {

    @Test
    void buildWithAllThreeFields() {
        final String actual = EmailBodyFactory.buildBodyFromWebApp(getCompleteRequest());
        String THREE_FIELDS_EMAIL = """
                Subject: TEST_SUBJECT
                            
                Body:
                TEST_BODY
                            
                Contact Info: TEST_CONTACT_INFO
                """;
        assertEquals(THREE_FIELDS_EMAIL, actual);
    }

    @Test
    void buildWithNullContactInfo() {
        final String actual = EmailBodyFactory.buildBodyFromWebApp(getRequestWithNoContactInfo());
        String TWO_FIELDS_EMAIL = """
                Subject: TEST_SUBJECT
                            
                Body:
                TEST_BODY
                            
                Contact Info: Not Provided
                """;
        assertEquals(TWO_FIELDS_EMAIL, actual);
    }

    @Test
    void testBuildBodyForExceptionNotification_Exception() {
        final Exception e = new Exception("TEST_EXCEPTION_MSG");
        final String partiallyExpected = String.format("""
                <p>Exception message: %s</p>
                <p>Stacktrace:</p>
                <p>
                    %s
                </p>
                </body></html>
                """, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
        final String actual = EmailBodyFactory.buildBodyForExceptionNotification(e);
        assertTrue(actual.contains(partiallyExpected));
    }

    @Test
    void testBuildBodyForExceptionNotification_IllegalArgumentException() {
        final Exception e = new IllegalArgumentException("TEST_EXCEPTION_MSG");
        final String partiallyExpected = String.format("""
                <p>Exception message: %s</p>
                <p>Stacktrace:</p>
                <p>
                    %s
                </p>
                </body></html>
                """, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
        final String actual = EmailBodyFactory.buildBodyForExceptionNotification(e);
        assertTrue(actual.contains(partiallyExpected));
    }

    private EmailRequest getCompleteRequest() {
        return new EmailRequest("TEST_SUBJECT", "TEST_BODY", "TEST_CONTACT_INFO");
    }

    private EmailRequest getRequestWithNoContactInfo() {
        return new EmailRequest("TEST_SUBJECT", "TEST_BODY", null);
    }
}
