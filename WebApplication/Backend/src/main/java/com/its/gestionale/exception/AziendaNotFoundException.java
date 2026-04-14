package com.its.gestionale.exception;

/**
 * Eccezione di dominio lanciata quando una azienda richiesta non esiste.
 *
 * Viene sollevata tipicamente nel service quando una ricerca per id non
 * restituisce risultati. Nel layer web viene poi tradotta in risposta HTTP 404
 * dai gestori centralizzati delle eccezioni.
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
