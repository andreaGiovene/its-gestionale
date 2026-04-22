package com.its.gestionale.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.TirocinioDTO;
import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.Tirocinio;
import com.its.gestionale.exception.AllievoNotFoundException;
import com.its.gestionale.exception.AziendaNotFoundException;
import com.its.gestionale.repository.AllievoRepository;
import com.its.gestionale.repository.AziendaRepository;
import com.its.gestionale.repository.TirocinioRepository;

@Service
public class TirocinioService {

    private final TirocinioRepository repo;
    private final AllievoRepository allievoRepo;
    private final AziendaRepository aziendaRepo;

    public TirocinioService(TirocinioRepository repo,
                           AllievoRepository allievoRepo,
                           AziendaRepository aziendaRepo) {
        this.repo = repo;
        this.allievoRepo = allievoRepo;
        this.aziendaRepo = aziendaRepo;
    }

    // 🔹 GET ALL
    @Transactional(readOnly = true)
    public List<TirocinioDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(TirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY ID
    @Transactional(readOnly = true)
    public TirocinioDTO findById(Integer id) {
        return TirocinioDTO.fromEntity(
                repo.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Tirocinio con id " + id + " non trovato"))
        );
    }

    // 🔹 GET BY ALLIEVO
    @Transactional(readOnly = true)
    public List<TirocinioDTO> findByAllievoId(Integer id) {

        if (!allievoRepo.existsById(id)) {
            throw new AllievoNotFoundException(id);
        }

        return repo.findByAllievo_Id(id)
                .stream()
                .map(TirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY AZIENDA
    @Transactional(readOnly = true)
    public List<TirocinioDTO> findByAziendaId(Integer id) {

        if (!aziendaRepo.existsById(id)) {
            throw new AziendaNotFoundException(id);
        }

        return repo.findByAzienda_Id(id)
                .stream()
                .map(TirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 GET BY PERIODO
    @Transactional(readOnly = true)
    public List<TirocinioDTO> findByPeriodo(LocalDate start, LocalDate end) {

        if (start == null || end == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date obbligatorie");
        }

        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Intervallo non valido");
        }

        return repo
                .findByDataInizioGreaterThanEqualAndDataFineLessThanEqual(start, end)
                .stream()
                .map(TirocinioDTO::fromEntity)
                .toList();
    }

    // 🔹 CREATE
    @Transactional
    public TirocinioDTO create(Integer allievoId, Integer aziendaId, Tirocinio t) {

        validate(t);

        Allievo allievo = allievoRepo.findById(allievoId)
                .orElseThrow(() -> new AllievoNotFoundException(allievoId));

        Azienda azienda = aziendaRepo.findById(aziendaId)
                .orElseThrow(() -> new AziendaNotFoundException(aziendaId));

        t.setAllievo(allievo);
        t.setAzienda(azienda);

        return TirocinioDTO.fromEntity(repo.save(t));
    }

    // 🔹 UPDATE
    @Transactional
    public TirocinioDTO update(Integer id, Tirocinio t) {

        Tirocinio existing = repo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Tirocinio con id " + id + " non trovato"));

        existing.setDataInizio(t.getDataInizio());
        existing.setDataFine(t.getDataFine());
        existing.setTipo(t.getTipo());
        existing.setFrequenza(t.getFrequenza());
        existing.setEsito(t.getEsito());

        return TirocinioDTO.fromEntity(repo.save(existing));
    }

    // 🔹 DELETE
    @Transactional
    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Tirocinio con id " + id + " non trovato");
        }
        repo.deleteById(id);
    }

    // 🔹 VALIDAZIONE
    private void validate(Tirocinio t) {
        if (t == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload obbligatorio");
        }

        if (t.getDataInizio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data inizio obbligatoria");
        }
    }
}