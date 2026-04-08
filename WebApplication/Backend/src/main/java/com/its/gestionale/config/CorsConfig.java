package com.its.gestionale.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * Configurazione centralizzata delle policy CORS dell'applicazione.
 * <p>
 * I valori sono letti da proprietà esterne (application.properties o variabili
 * di ambiente) per poter adattare facilmente ambienti diversi senza modifiche
 * al codice.
 */
public class CorsConfig implements WebMvcConfigurer {

    /** Origini consentite (CSV), ad es. "http://localhost:4200,https://dominio.it". */
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    /** Metodi HTTP consentiti (CSV), con fallback di default. */
    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    /** Header consentiti (CSV), con fallback di default. */
    @Value("${app.cors.allowed-headers:Authorization,Content-Type,Accept,Origin}")
    private String allowedHeaders;

    /** Indica se includere cookie/credenziali nelle richieste cross-origin. */
    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    /** Tempo (in secondi) per cui la risposta preflight puo essere cacheata dal browser. */
    @Value("${app.cors.max-age:3600}")
    private long maxAge;

    /**
     * Applica la policy CORS agli endpoint REST principali.
     * <p>
     * In questo progetto la configurazione e applicata sia alle rotte funzionali
     * (/api/**) sia alle rotte di autenticazione (/auth/**).
     */
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

    /**
     * Converte una stringa CSV in array di stringhe pulite, ignorando valori vuoti.
     */
    private String[] splitCsv(String csvValue) {
        return Arrays.stream(csvValue.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);
    }
}
