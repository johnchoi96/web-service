package io.github.johnchoi96.webservice.configs.swaggerui;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@Configuration
@RequiredArgsConstructor
public class SwaggerUiConfig {

    private final Environment environment;

    @Bean
    public OpenAPI springShopOpenAPI() {
        final OpenAPI config = new OpenAPI()
                .addServersItem(new Server().url("https://web-service.johnchoi96.com"))
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
        if (environment.acceptsProfiles(Profiles.of("local"))) {
            config.addServersItem(new Server().url("http://localhost:8080"));
        }
        return config;
    }
}
