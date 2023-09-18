package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import io.github.johnchoi96.webservice.models.metalprice.Rates;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailBodyFactoryTest {

    @Test
    public void buildWithAllThreeFields() {
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
    public void buildWithNullContactInfo() {
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
    public void buildForMetalPriceIncreased() {
        final LocalDate prevDate = LocalDate.of(2023, 9, 14);
        final LocalDate todayDate = LocalDate.of(2023, 9, 15);
        final MetalPriceResponse prevRate = getDummyPrevRate();
        final MetalPriceResponse todayRate = getDummyTodayRate();
        final String expectedBody = """
                <html><body>
                <h3>Gold Price is lower today!</h3>
                <p>
                    Previous Gold Rate on 09/14/2023: $2.34
                    <br />
                    Today's Gold Rate on 09/15/2023: $3.45
                    <br />
                    Difference: $1.11
                    <br />
                    For more info, <a href='https://www.google.com/search?q=gold+price+right+now'>Click Here</a>
                    <br />
                </p>
                </body></html>
                """;
        final String actual = EmailBodyFactory.buildBodyForMetalPrice(prevDate, todayDate, prevRate, todayRate);
        assertEquals(expectedBody, actual);
    }

    @Test
    public void buildForMetalPriceDecreased() {
        final LocalDate prevDate = LocalDate.of(2023, 9, 14);
        final LocalDate todayDate = LocalDate.of(2023, 9, 15);
        final MetalPriceResponse prevRate = getDummyTodayRate();
        final MetalPriceResponse todayRate = getDummyPrevRate();
        final String expectedBody = """
                <html><body>
                <h3>Gold Price is lower today!</h3>
                <p>
                    Previous Gold Rate on 09/14/2023: $3.45
                    <br />
                    Today's Gold Rate on 09/15/2023: $2.34
                    <br />
                    Difference: $-1.11
                    <br />
                    For more info, <a href='https://www.google.com/search?q=gold+price+right+now'>Click Here</a>
                    <br />
                </p>
                </body></html>
                """;
        final String actual = EmailBodyFactory.buildBodyForMetalPrice(prevDate, todayDate, prevRate, todayRate);
        assertEquals(expectedBody, actual);
    }

    private MetalPriceResponse getDummyPrevRate() {
        final MetalPriceResponse response = new MetalPriceResponse();
        final Rates rate = new Rates();
        rate.setUsd(2.34);
        response.setRates(rate);
        return response;
    }

    private MetalPriceResponse getDummyTodayRate() {
        final MetalPriceResponse response = new MetalPriceResponse();
        final Rates rate = new Rates();
        rate.setUsd(3.45);
        response.setRates(rate);
        return response;
    }

    private EmailRequest getCompleteRequest() {
        return new EmailRequest("TEST_SUBJECT", "TEST_BODY", "TEST_CONTACT_INFO");
    }

    private EmailRequest getRequestWithNoContactInfo() {
        return new EmailRequest("TEST_SUBJECT", "TEST_BODY", null);
    }
}
