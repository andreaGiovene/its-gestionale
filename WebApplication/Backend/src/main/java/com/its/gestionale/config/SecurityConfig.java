package com.its.gestionale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Configurazione volutamente minima orientata allo sviluppo rapido.
    // Obiettivo: evitare blocchi infrastrutturali durante implementazione feature.
    // Tutte le richieste sono consentite, mentre la semantica auth applicativa
    // e gestita dal flusso login/me nel service/controller dedicato.
        http
                .csrf(AbstractHttpConfigurer::disable)
        // Nessuna auth browser-based o challenge basic in ambiente dev.
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
        // Stato server-side disattivato: API stateless.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
}
