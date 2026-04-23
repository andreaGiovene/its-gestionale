package com.its.gestionale.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.ColloquioTirocinioDTO;
import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.ColloquioTirocinio;
import com.its.gestionale.entity.enums.StatoEsitoColloquio;
import com.its.gestionale.entity.enums.TipoEventoColloquio;
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

    // 🔹 GET ALL
    @Transactional(readOnly = true)
    public List<ColloquioTirocinioDTO> findAll() {
        return colloquioRepository.findAll()
                .stream()
                .map(ColloquioTirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY ID
    @Transactional(readOnly = true)
    public ColloquioTirocinioDTO findById(Integer id) {
        ColloquioTirocinio colloquio = colloquioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Colloquio con id " + id + " non trovato"));

        return ColloquioTirocinioDTO.fromEntity(colloquio);
    }

    // 🔹 GET BY ALLIEVO
    @Transactional(readOnly = true)
    public List<ColloquioTirocinioDTO> findByAllievoId(Integer allievoId) {

        if (!allievoRepository.existsById(allievoId)) {
            throw new AllievoNotFoundException(allievoId);
        }

        return colloquioRepository.findByAllievoId(allievoId)
                .stream()
                .map(ColloquioTirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY AZIENDA
    @Transactional(readOnly = true)
    public List<ColloquioTirocinioDTO> findByAziendaId(Integer aziendaId) {

        if (!aziendaRepository.existsById(aziendaId)) {
            throw new AziendaNotFoundException(aziendaId);
        }

        return colloquioRepository.findByAziendaId(aziendaId)
                .stream()
                .map(ColloquioTirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY ALLIEVO + AZIENDA
    @Transactional(readOnly = true)
    public List<ColloquioTirocinioDTO> findByAllievoIdAndAziendaId(Integer allievoId, Integer aziendaId) {

        if (!allievoRepository.existsById(allievoId)) {
            throw new AllievoNotFoundException(allievoId);
        }

        if (!aziendaRepository.existsById(aziendaId)) {
            throw new AziendaNotFoundException(aziendaId);
        }

        return colloquioRepository.findByAllievoIdAndAziendaId(allievoId, aziendaId)
                .stream()
                .map(ColloquioTirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY PERIODO
    @Transactional(readOnly = true)
    public List<ColloquioTirocinioDTO> findByPeriodo(LocalDate start, LocalDate end) {

        if (start == null || end == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le date sono obbligatorie");
        }

        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intervallo date non valido");
        }

        return colloquioRepository.findByDataColloquioBetween(start, end)
                .stream()
                .map(ColloquioTirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 CREATE
    @Transactional
    public ColloquioTirocinioDTO create(Integer allievoId, Integer aziendaId, ColloquioTirocinio request) {

        validateRichiesta(request);

        Allievo allievo = allievoRepository.findById(allievoId)
                .orElseThrow(() -> new AllievoNotFoundException(allievoId));

        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new AziendaNotFoundException(aziendaId));

        ColloquioTirocinio colloquio = new ColloquioTirocinio();
        colloquio.setAllievo(allievo);
        colloquio.setAzienda(azienda);
        colloquio.setDataColloquio(request.getDataColloquio());
        colloquio.setTipoEvento(
            request.getTipoEvento() != null
                ? request.getTipoEvento()
                : TipoEventoColloquio.FUORI_MATCHING_DAY
        );
        colloquio.setEsito(
                request.getEsito() != null
                        ? request.getEsito()
                        : StatoEsitoColloquio.IN_ATTESA
        );
        colloquio.setNoteFeedback(request.getNoteFeedback());

        return toDto(colloquioRepository.save(colloquio));
    }

    // 🔹 UPDATE
    @Transactional
    public ColloquioTirocinioDTO update(Integer id, ColloquioTirocinio request) {

        validateRichiesta(request);

        ColloquioTirocinio colloquio = colloquioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Colloquio con id " + id + " non trovato"));

        colloquio.setDataColloquio(request.getDataColloquio());
        colloquio.setTipoEvento(
            request.getTipoEvento() != null
                ? request.getTipoEvento()
                : TipoEventoColloquio.FUORI_MATCHING_DAY
        );
        colloquio.setEsito(
                request.getEsito() != null
                        ? request.getEsito()
                        : StatoEsitoColloquio.IN_ATTESA
        );
        colloquio.setNoteFeedback(request.getNoteFeedback());

        return toDto(colloquioRepository.save(colloquio));
    }

    // 🔹 DELETE
    @Transactional
    public void deleteById(Integer id) {

        if (!colloquioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Colloquio con id " + id + " non trovato");
        }

        colloquioRepository.deleteById(id);
    }

    // 🔹 VALIDAZIONE
    private void validateRichiesta(ColloquioTirocinio request) {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload obbligatorio");
        }

        if (request.getDataColloquio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data colloquio obbligatoria");
        }
    }

    private ColloquioTirocinioDTO toDto(ColloquioTirocinio colloquio) {
        return ColloquioTirocinioDTO.fromEntity(colloquio);
    }

}
