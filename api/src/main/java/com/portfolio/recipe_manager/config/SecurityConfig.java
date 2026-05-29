package com.portfolio.recipe_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Create CSP header
        // self -> own server origin (none for now, but allow just in case)
        // data: -> base64 encoded images (for recipe image)
        // https: -> images from any secure HTTPS external site
        httpSecurity
                .cors(Customizer.withDefaults()) // use CORS defined in WebConfig
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp ->
                                csp.policyDirectives("img-src 'self' data: https:; default-src 'self';")))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Needs proper configuration for endpoint permissions, but should
                        // be okay for the purpose of this project as "permitAll()".
                );
        return httpSecurity.build();
    }
}
