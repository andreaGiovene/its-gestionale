package com.its.gestionale.exception;

public class CorsoNotFoundException extends RuntimeException {

    public CorsoNotFoundException(Integer id) {
        super("Corso con id " + id + " non trovato");
    }
}
