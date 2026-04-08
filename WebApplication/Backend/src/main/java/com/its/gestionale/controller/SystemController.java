package com.its.gestionale.controller;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * Controller di sistema con endpoint pubblici per verifica disponibilita servizio.
 * <p>
 * Espone una root informativa e un endpoint health-check minimale.
 */
public class SystemController {

    /**
     * Endpoint root applicativo.
     *
     * @return metadati runtime del servizio (nome, stato e timestamp corrente)
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(Map.of(
                "service", "gestionale-backend",
                "status", "up",
                "timestamp", OffsetDateTime.now().toString()));
    }

    /**
     * Endpoint di health-check per monitoraggio rapido (liveness).
     *
     * @return stato sintetico del servizio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
