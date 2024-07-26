package io.github.johnchoi96.webservice.configs.swaggerui;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

public class SwaggerUiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("web-service")
                        .description("@johnchoi96's personal web service")
                        .contact(new Contact()
                                .name("@johnchoi96")
                                .email("johnchoi1003@icloud.com")
                                .url("https://johnchoi96.com/")
                        )
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")
                        )
                );
    }
}
