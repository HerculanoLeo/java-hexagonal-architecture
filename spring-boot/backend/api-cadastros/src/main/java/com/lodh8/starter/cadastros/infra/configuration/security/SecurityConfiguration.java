package com.lodh8.starter.cadastros.infra.configuration.security;

import com.lodh8.starter.cadastros.infra.attributes.WebAttributes;
import com.lodh8.starter.cadastros.infra.logging.LoggingMdcFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    LoggingMdcFilter loggingMdcFilter() {
        return new LoggingMdcFilter();
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            @Qualifier("authenticationManagerDecorator") AuthenticationManager manager,
            LoggingMdcFilter loggingMdcFilter
    ) {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.requestMatchers(
                                        "/openapi/*",
                                        "/openapi/*/*",
                                        "/*/swagger-ui/*",
                                        "/*/swagger-ui.html/*",
                                        "/*/swagger-resources/*",
                                        "/v3/api-docs",
                                        "/v3/api-docs/*",
                                        "/actuator/health"
                                ).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement((management) -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(configurer ->
                        configurer.jwt((jwt) -> jwt.authenticationManager(manager))
                )
                .cors(Customizer.withDefaults())
                .addFilterAfter(loggingMdcFilter, BearerTokenAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public CorsConfiguration corsConfiguration(WebAttributes attributes) {
        var cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.setAllowedOrigins(attributes.getCors().getAllowedOrigins());
        cors.setAllowedHeaders(attributes.getCors().getAllowedHeaders());
        cors.setAllowedMethods(attributes.getCors().getAllowedMethods());
        cors.setExposedHeaders(attributes.getCors().getExposedHeaders());
        return cors;
    }

}
