package io.github.johnchoi96.webservice.configs.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/actuator/**").authenticated()
                                .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Configuring CSRF token repository
                )
                .httpBasic(httpBasic -> {
                    httpBasic.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    httpBasic.realmName("Actuator Realm");
                });
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        if (environment.acceptsProfiles(Profiles.of("local"))) {
            UserDetails user = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("admin1234"))
                    .roles("USER")
                    .build();
            return new InMemoryUserDetailsManager(user);
        } else {
            // TODO: call user service to get the User Details information
            return new InMemoryUserDetailsManager();
        }
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