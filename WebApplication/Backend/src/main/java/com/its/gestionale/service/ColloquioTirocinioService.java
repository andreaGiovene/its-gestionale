package com.its.gestionale.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.ColloquioTirocinio;
import com.its.gestionale.entity.enums.StatoEsitoColloquio;
import com.its.gestionale.exception.AllievoNotFoundException;
import com.its.gestionale.exception.AziendaNotFoundException;
import com.its.gestionale.repository.AllievoRepository;
import com.its.gestionale.repository.AziendaRepository;
import com.its.gestionale.repository.ColloquioTirocinioRepository;

/**
 * Service applicativo per la gestione dei colloqui di tirocinio.
 *
 * <p>Centralizza la logica di validazione e di risoluzione delle relazioni
 * obbligatorie (allievo e azienda), mantenendo i controller sottili.
 */
@Service
public class ColloquioTirocinioService {

    private final ColloquioTirocinioRepository colloquioRepository;
    private final AllievoRepository allievoRepository;
    private final AziendaRepository aziendaRepository;

    public ColloquioTirocinioService(
            ColloquioTirocinioRepository colloquioRepository,
            AllievoRepository allievoRepository,
            AziendaRepository aziendaRepository) {
        this.colloquioRepository = colloquioRepository;
        this.allievoRepository = allievoRepository;
        this.aziendaRepository = aziendaRepository;
    }

    /** Restituisce tutti i colloqui presenti a sistema. */
    @Transactional(readOnly = true)
    public List<ColloquioTirocinio> findAll() {
        return colloquioRepository.findAll();
    }

    /** Recupera un colloquio per identificativo. */
    @Transactional(readOnly = true)
    public ColloquioTirocinio findById(Integer id) {
        return colloquioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Colloquio con id " + id + " non trovato"));
    }

    /** Restituisce i colloqui associati a uno specifico allievo. */
    @Transactional(readOnly = true)
    public List<ColloquioTirocinio> findByAllievoId(Integer allievoId) {
        return colloquioRepository.findByAllievoId(allievoId);
    }

    /** Restituisce i colloqui associati a una specifica azienda. */
    @Transactional(readOnly = true)
    public List<ColloquioTirocinio> findByAziendaId(Integer aziendaId) {
        return colloquioRepository.findByAziendaId(aziendaId);
    }

    /**
     * Ricerca colloqui in un intervallo di date inclusivo.
     *
     * @throws IllegalArgumentException se una delle date e nulla o se l'intervallo e invertito
     */
    @Transactional(readOnly = true)
    public List<ColloquioTirocinio> findByPeriodo(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Le date di inizio/fine sono obbligatorie");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("La data fine non puo essere precedente alla data inizio");
        }

        return colloquioRepository.findByDataColloquioBetween(start, end);
    }

    /**
     * Crea un nuovo colloquio collegandolo ad allievo e azienda esistenti.
     *
     * <p>Se l'esito non viene valorizzato in input, viene applicato
    * automaticamente {@link StatoEsitoColloquio#IN_ATTESA}.
     */
    @Transactional
    public ColloquioTirocinio create(Integer allievoId, Integer aziendaId, ColloquioTirocinio request) {
        validateRichiesta(request);

        Allievo allievo = allievoRepository.findById(allievoId)
                .orElseThrow(() -> new AllievoNotFoundException(allievoId));

        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new AziendaNotFoundException(aziendaId));

        ColloquioTirocinio colloquio = new ColloquioTirocinio();
        colloquio.setAllievo(allievo);
        colloquio.setAzienda(azienda);
        colloquio.setDataColloquio(request.getDataColloquio());
        colloquio.setTipoEvento(request.getTipoEvento());
        colloquio.setEsito(request.getEsito() != null ? request.getEsito() : StatoEsitoColloquio.IN_ATTESA);
        colloquio.setNoteFeedback(request.getNoteFeedback());

        return colloquioRepository.save(colloquio);
    }

    /**
     * Aggiorna un colloquio esistente.
     *
     * <p>Le relazioni con allievo e azienda restano invariate in questa prima versione
     * del service; l'update copre i campi gestionali del colloquio.
     */
    @Transactional
    public ColloquioTirocinio update(Integer id, ColloquioTirocinio request) {
        validateRichiesta(request);

        ColloquioTirocinio colloquio = findById(id);
        colloquio.setDataColloquio(request.getDataColloquio());
        colloquio.setTipoEvento(request.getTipoEvento());
        colloquio.setEsito(request.getEsito() != null ? request.getEsito() : StatoEsitoColloquio.IN_ATTESA);
        colloquio.setNoteFeedback(request.getNoteFeedback());

        return colloquioRepository.save(colloquio);
    }

    /** Elimina un colloquio se esistente. */
    @Transactional
    public void deleteById(Integer id) {
        if (!colloquioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colloquio con id " + id + " non trovato");
        }

        colloquioRepository.deleteById(id);
    }

    private void validateRichiesta(ColloquioTirocinio request) {
        if (request == null) {
            throw new IllegalArgumentException("Il payload colloquio e obbligatorio");
        }

        if (request.getDataColloquio() == null) {
            throw new IllegalArgumentException("La data colloquio e obbligatoria");
        }
    }
}
