package com.johnchoi96.webservice.configs.swaggerui;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.LinkedList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerUiConfig {

    private final Environment environment;

    @Bean
    public OpenAPI springShopOpenAPI() {
        final List<Server> servers = new LinkedList<>();
        final Server deployedServer = new Server().url("https://web-service.johnchoi96.com");
        final Server localServer = new Server().url("http://localhost:8080");
        servers.add(deployedServer);
        if (environment.acceptsProfiles(Profiles.of("local"))) {
            servers.addFirst(localServer);
        }

        final OpenAPI config = new OpenAPI()
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
                )
                .components(new Components()
                        .addSecuritySchemes(
                                "basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        )
                );

        config.setServers(servers);
        return config;
    }
}
