package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailBodyFactoryTest {

    @Test
    public void buildWithAllThreeFields() {
        final String actual = EmailBodyFactory.build(getCompleteRequest());
        String THREE_FIELDS_EMAIL = """
                Subject: TEST_SUBJECT
                            
                Body:
                TEST_BODY
                            
                Contact Info: TEST_CONTACT_INFO
                """;
        assertEquals(THREE_FIELDS_EMAIL, actual);
    }

    @Test
    public void buildWithNullContactInfo() {
        final String actual = EmailBodyFactory.build(getRequestWithNoContactInfo());
        String TWO_FIELDS_EMAIL = """
                Subject: TEST_SUBJECT
                            
                Body:
                TEST_BODY
                            
                Contact Info: Not Provided
                """;
        assertEquals(TWO_FIELDS_EMAIL, actual);
    }

    private EmailRequest getCompleteRequest() {
        final EmailRequest request = new EmailRequest();
        request.setSubject("TEST_SUBJECT");
        request.setBody("TEST_BODY");
        request.setContactInfo("TEST_CONTACT_INFO");
        return request;
    }

    private EmailRequest getRequestWithNoContactInfo() {
        final EmailRequest request = new EmailRequest();
        request.setSubject("TEST_SUBJECT");
        request.setBody("TEST_BODY");
        return request;
    }
}
