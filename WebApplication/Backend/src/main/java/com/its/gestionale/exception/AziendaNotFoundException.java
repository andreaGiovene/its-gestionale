package com.its.gestionale.exception;

public class AziendaNotFoundException extends RuntimeException {

    public AziendaNotFoundException(Integer id) {
        super("Azienda con id " + id + " non trovata");
    }
}
