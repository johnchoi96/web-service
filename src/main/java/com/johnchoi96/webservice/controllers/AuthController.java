package com.johnchoi96.webservice.controllers;

import com.johnchoi96.webservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/check-access")
    public ResponseEntity<String> checkUserAccess(@AuthenticationPrincipal Jwt jwt) {
        log.info("GET /api/auth/check-access");
        final String email = jwt.getClaimAsString("email");

        if (!userService.isActiveUser(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied");
        }

        return ResponseEntity.ok().build();
    }
}
