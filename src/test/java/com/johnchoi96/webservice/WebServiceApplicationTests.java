package com.johnchoi96.webservice;

import com.johnchoi96.webservice.configs.MockedBeansConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(MockedBeansConfigs.class)
class WebServiceApplicationTests {

    @Test
    void verifyMainClassId() {
        final String CLASS_ID = "com.johnchoi96.webservice.WebServiceApplication";
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
