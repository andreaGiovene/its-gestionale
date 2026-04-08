package com.its.gestionale.exception;

/**
 * Eccezione di dominio lanciata quando una azienda richiesta non esiste.
 */
public class AziendaNotFoundException extends RuntimeException {

    /**
     * Crea un'eccezione con messaggio contestualizzato all'id ricercato.
     *
     * @param id identificativo dell'azienda non trovata
     */
    public AziendaNotFoundException(Integer id) {
        super("Azienda con id " + id + " non trovata");
    }
}
