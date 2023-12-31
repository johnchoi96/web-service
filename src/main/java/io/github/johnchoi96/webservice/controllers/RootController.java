package io.github.johnchoi96.webservice.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
@Hidden
public class RootController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> root() {
        log.info("GET /");
        final String htmlBody = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                            <title>@johnchoi96's web service</title>
                            </head>
                            <body>
                            <h1>@johnchoi96's web service</h1>
                            <p>
                                You've reached johnchoi96's web service.
                                <br />
                                For documentations, refer to:
                                <a href='/api-docs'>SwaggerUI</a>
                            </p>
                            <h4>Links:</h4>
                            <p>
                                <a href='https://johnchoi96.github.io/'>About Me</a>
                                <br />
                                <a href='https://www.github.com/johnchoi96'>GitHub Profile</a>
                                <br />
                            </p>
                           
                            </body>
                            </html>
                """;
        return ResponseEntity.ok(htmlBody);
    }
}
