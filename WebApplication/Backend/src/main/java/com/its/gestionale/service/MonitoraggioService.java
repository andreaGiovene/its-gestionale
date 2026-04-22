package com.its.gestionale.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.MonitoraggioDTO;
import com.its.gestionale.entity.Monitoraggio;
import com.its.gestionale.entity.Tirocinio;
import com.its.gestionale.repository.MonitoraggioRepository;
import com.its.gestionale.repository.TirocinioRepository;

/**
 * Servizio per la gestione dei monitoraggi.
 */

@Service
public class MonitoraggioService {

    private final MonitoraggioRepository repo;
    private final TirocinioRepository tirocinioRepo;

    public MonitoraggioService(MonitoraggioRepository repo,
                               TirocinioRepository tirocinioRepo) {
        this.repo = repo;
        this.tirocinioRepo = tirocinioRepo;
    }

    /**
     * CRUD per i monitoraggi.
     * Include validazione dei dati e gestione degli errori tramite ResponseStatusException.
     * I monitoraggi sono sempre associati a un tirocinio, quindi nelle operazioni di create/update
     * è necessario verificare l'esistenza del tirocinio associato.
     */
    @Transactional(readOnly = true)
    public List<MonitoraggioDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(MonitoraggioDTO::fromEntity)
                .toList();
    }

    /**
     * GET BY ID per i monitoraggi.
     * Include validazione dell'esistenza del monitoraggio tramite ResponseStatusException.
     */
    @Transactional(readOnly = true)
    public MonitoraggioDTO findById(Integer id) {
        return MonitoraggioDTO.fromEntity(
                repo.findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Monitoraggio con id " + id + " non trovato"))
        );
    }

    /**
     * GET BY TIROCINIO per i monitoraggi.
     * Include validazione dell'esistenza del tirocinio tramite ResponseStatusException.
     */
    @Transactional(readOnly = true)
    public List<MonitoraggioDTO> findByTirocinioId(Integer tirocinioId) {

        if (!tirocinioRepo.existsById(tirocinioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Tirocinio con id " + tirocinioId + " non trovato");
        }

        return repo.findByTirocinioId(tirocinioId)
                .stream()
                .map(MonitoraggioDTO::fromEntity)
                .toList();
    }

    /**
     * CREATE per i monitoraggi.
     * Include validazione dell'esistenza del tirocinio tramite ResponseStatusException.
     */
    @Transactional
    public MonitoraggioDTO create(Integer tirocinioId, Monitoraggio m) {

        validate(m);

        Tirocinio tirocinio = tirocinioRepo.findById(tirocinioId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Tirocinio con id " + tirocinioId + " non trovato"));

        m.setTirocinio(tirocinio);

        return MonitoraggioDTO.fromEntity(repo.save(m));
    }

    /**
     * UPDATE per i monitoraggi.
     * Include validazione dell'esistenza del monitoraggio tramite ResponseStatusException.
     */
    @Transactional
    public MonitoraggioDTO update(Integer id, Monitoraggio m) {

        validate(m);

        Monitoraggio existing = repo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Monitoraggio con id " + id + " non trovato"));

        existing.setDataMonitoraggio(m.getDataMonitoraggio());
        existing.setResponsabileId(m.getResponsabileId());
        existing.setNote(m.getNote());

        return MonitoraggioDTO.fromEntity(repo.save(existing));
    }

    /**
     * DELETE per i monitoraggi.
     * Include validazione dell'esistenza del monitoraggio tramite ResponseStatusException.
     */
    @Transactional
    public void delete(Integer id) {

        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Monitoraggio con id " + id + " non trovato");
        }

        repo.deleteById(id);
    }

    /**
     * VALIDAZIONE per i monitoraggi.
     * Include validazione del payload e del data monitoraggio.
     * @param m monitoraggio da validare
     */
    private void validate(Monitoraggio m) {

        if (m == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload obbligatorio");
        }

        if (m.getDataMonitoraggio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Data monitoraggio obbligatoria");
        }
    }

    @Transactional(readOnly = true)
    /**
     * PIANIFICA PER CAP per i monitoraggi.
     * Ordina i monitoraggi per CAP dell'azienda associata al tirocinio.
     * @return lista ordinateata per CAP
     */
    public List<MonitoraggioDTO> pianificaPerCap() {

    return repo.findAll()
            .stream()
            // filtro sicurezza: evita null chain
            .filter(m -> m.getTirocinio() != null &&
                         m.getTirocinio().getAzienda() != null &&
                         m.getTirocinio().getAzienda().getCap() != null)
            
            // ordinamento per CAP
            .sorted((m1, m2) -> {
                String cap1 = m1.getTirocinio().getAzienda().getCap();
                String cap2 = m2.getTirocinio().getAzienda().getCap();
                return cap1.compareTo(cap2);
            })

            .map(MonitoraggioDTO::fromEntity)
            .toList();
    }
}