package com.its.gestionale.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${app.cors.allowed-headers:Authorization,Content-Type,Accept,Origin}")
    private String allowedHeaders;

    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${app.cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(splitCsv(allowedOrigins))
                .allowedMethods(splitCsv(allowedMethods))
                .allowedHeaders(splitCsv(allowedHeaders))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);

        registry.addMapping("/auth/**")
            .allowedOrigins(splitCsv(allowedOrigins))
            .allowedMethods(splitCsv(allowedMethods))
            .allowedHeaders(splitCsv(allowedHeaders))
            .allowCredentials(allowCredentials)
            .maxAge(maxAge);
    }

    private String[] splitCsv(String csvValue) {
        return Arrays.stream(csvValue.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);
    }
}
