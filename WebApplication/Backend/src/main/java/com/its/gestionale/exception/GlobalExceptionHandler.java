package com.its.gestionale.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Gestore centralizzato delle eccezioni per tutte le API REST.
 *
 * Converte eccezioni tecniche e di dominio in una risposta uniforme
 * {@link ApiErrorResponse}, cosi il frontend puo gestire gli errori in modo
 * prevedibile.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Traduce l'eccezione di allievo non trovato in HTTP 404.
     */
    @ExceptionHandler(AllievoNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAllievoNotFound(
            AllievoNotFoundException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    /**
     * Traduce l'eccezione di corso non trovato in HTTP 404.
     */
    @ExceptionHandler(CorsoNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCorsoNotFound(
            CorsoNotFoundException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    /**
     * Traduce l'eccezione di azienda non trovata in HTTP 404.
     */
    @ExceptionHandler(AziendaNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAziendaNotFound(
            AziendaNotFoundException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    /**
     * Traduce l'eccezione di utente non trovato in HTTP 404.
     */
    @ExceptionHandler(UtenteNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUtenteNotFound(
            UtenteNotFoundException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    /**
     * Raccoglie e restituisce gli errori di validazione input (HTTP 400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildError(HttpStatus.BAD_REQUEST, "Input non valido", request.getRequestURI(), errors);
    }

    /**
     * Gestisce errori di argomento non valido generati dall'applicazione.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    /**
     * Propaga il codice HTTP dichiarato nelle {@link ResponseStatusException}.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request) {
        log.warn("ResponseStatusException on {}: {}", request.getRequestURI(), ex.getMessage());
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();
        return buildError(status, message, request.getRequestURI(), null);
    }

    /**
     * Fallback per eccezioni non previste: ritorna HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {
        log.error("Unhandled exception on {}", request.getRequestURI(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno del server",
                request.getRequestURI(),
                null);
    }

    /**
     * Crea il payload errore standard e lo incapsula nella response HTTP.
     */
    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors) {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validationErrors
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleEnumError(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String message = "Formato JSON non valido";
        String rawMessage = ex.getMessage();

        if (rawMessage != null && rawMessage.contains("RuoloContatto")) {
            message = "Valore non valido per il campo 'ruolo'";
        } else if (rawMessage != null && rawMessage.contains("StatoTirocinio")) {
            message = "Valore non valido per il campo 'esito'. Valori ammessi: IN_CORSO, CONCLUSO, INTERROTTO";
        }

        return buildError(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                null
        );
    }
}
