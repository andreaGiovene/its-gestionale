package com.its.gestionale.exception;

/**
 * Eccezione di dominio lanciata quando un corso richiesto non esiste.
 */
public class CorsoNotFoundException extends RuntimeException {

    /**
     * Crea un'eccezione con messaggio contestualizzato all'id ricercato.
     *
     * @param id identificativo del corso non trovato
     */
    public CorsoNotFoundException(Integer id) {
        super("Corso con id " + id + " non trovato");
    }
}
