package com.its.gestionale.exception;

/**
 * Eccezione di dominio lanciata quando un utente richiesto non esiste.
 */
public class UtenteNotFoundException extends RuntimeException {

    /**
     * Crea un'eccezione con messaggio contestualizzato all'id ricercato.
     *
     * @param id identificativo dell'utente non trovato
     */
    public UtenteNotFoundException(Integer id) {
        super("Utente con id " + id + " non trovato");
    }

    /**
     * Crea un'eccezione con messaggio contestualizzato all'username ricercato.
     *
     * @param username username dell'utente non trovato
     */
    public UtenteNotFoundException(String username) {
        super("Utente con username " + username + " non trovato");
    }
}
