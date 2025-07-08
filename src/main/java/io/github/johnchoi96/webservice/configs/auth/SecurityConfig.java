package io.github.johnchoi96.webservice.configs.auth;

import io.github.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment environment;

    private final AdminKeysProperties adminKeysProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.PUT, "/api/resume/refresh").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/fcm/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/metal-price/trigger-report").hasRole("ADMIN")
                                .requestMatchers("/actuator/**").authenticated()
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable) // done on purpose until this is taken care of
                .httpBasic(httpBasic -> {
                    httpBasic.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    httpBasic.realmName("Actuator Realm");
                });
        return http.build();
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