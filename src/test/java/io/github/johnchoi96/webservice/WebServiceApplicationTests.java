package io.github.johnchoi96.webservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebServiceApplicationTests {

    @Test
    void contextLoads() {
        WebServiceApplication.main(new String[]{});
    }

    @Test
    void verifyMainClassId() {
        final String CLASS_ID = "io.github.johnchoi96.webservice.WebServiceApplication";
        assertEquals(CLASS_ID, WebServiceApplication.class.getName());
    }

    @Test
    void verifyMainMethodExists() {
        assertNotNull(
                Arrays.stream(WebServiceApplication.class.getMethods())
                        .filter(method -> method.getName().equals("main"))
                        .findFirst()
                        .orElse(null)
        );
    }
}
