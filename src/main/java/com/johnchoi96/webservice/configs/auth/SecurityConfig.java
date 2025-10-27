package com.johnchoi96.webservice.configs.auth;

import com.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment environment;

    private final AdminKeysProperties adminKeysProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // done on purpose until this is taken care of
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.PUT, "/api/resume/refresh").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/app-distribution/upload").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/fcm/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/fcm/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/metal-price/trigger-report").hasRole("ADMIN")
                                .requestMatchers("/actuator/**").authenticated()
                                .requestMatchers("/api/auth/**").authenticated()
                                .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> {
                    httpBasic.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    httpBasic.realmName("Actuator Realm");
                })
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwtConfigurer -> {
                            // No custom config for now
                        })
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://johnchoi96.com"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // important for cookies or auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin;
        if (environment.acceptsProfiles(Profiles.of("local"))) {
            // local dev user
            admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("admin1234"))
                    .roles("ADMIN")
                    .build();
        } else {
            // prod: use adminKey from admin-keys.yml
            admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode(adminKeysProperties.getAdminKey()))
                    .roles("ADMIN")
                    .build();
        }
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Actuator Realm");
        return entryPoint;
    }
}