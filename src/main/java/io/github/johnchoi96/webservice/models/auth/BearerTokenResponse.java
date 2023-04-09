package io.github.johnchoi96.webservice.models.auth;

import lombok.Data;

@Data
public class BearerTokenResponse {
    private String accessToken;

    private String tokenType;

    private int expiresIn;
}