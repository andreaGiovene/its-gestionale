package com.its.gestionale.exception;

/**
 * Eccezione di dominio lanciata quando un allievo richiesto non esiste.
 */
public class AllievoNotFoundException extends RuntimeException {

    /**
     * Crea un'eccezione con messaggio contestualizzato all'id ricercato.
     *
     * @param id identificativo dell'allievo non trovato
     */
    public AllievoNotFoundException(Integer id) {
        super("Allievo con id " + id + " non trovato");
    }
}
