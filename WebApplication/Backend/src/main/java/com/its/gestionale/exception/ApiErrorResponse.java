package com.its.gestionale.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Payload standard restituito dalle API in caso di errore.
 *
 * @param timestamp istante in cui l'errore e stato generato
 * @param status codice HTTP numerico
 * @param error descrizione testuale dello status HTTP
 * @param message dettaglio leggibile dell'errore
 * @param path endpoint richiesto che ha causato l'errore
 * @param validationErrors eventuali errori di validazione per campo
 */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}
