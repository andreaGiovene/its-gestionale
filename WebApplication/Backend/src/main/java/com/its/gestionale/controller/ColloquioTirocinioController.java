package com.its.gestionale.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.dto.ColloquioTirocinioDTO;
import com.its.gestionale.entity.ColloquioTirocinio;
import com.its.gestionale.service.ColloquioTirocinioService;

import jakarta.validation.Valid;

/**
 * REST Controller per la gestione dei colloqui di tirocinio.
 *
 * Espone operazioni CRUD e filtri su colloqui (ricerca per allievo, azienda, periodo).
 *
 * Endpoint disponibili:
 * - GET /api/colloqui - lista completa
 * - GET /api/colloqui/{id} - dettaglio singolo
 * - GET /api/colloqui?allievoId=X - colloqui di un allievo
 * - GET /api/colloqui?aziendaId=X - colloqui con un'azienda
 * - GET /api/colloqui?allievoId=X&aziendaId=Y - colloqui di un allievo presso un'azienda
 * - GET /api/colloqui?start=YYYY-MM-DD&end=YYYY-MM-DD - colloqui in un periodo
 * - POST /api/colloqui?allievoId=X&aziendaId=Y - creazione
 * - PUT /api/colloqui/{id} - aggiornamento
 * - DELETE /api/colloqui/{id} - eliminazione
 *
 * @see ColloquioTirocinioService
 * @see ColloquioTirocinioDTO
 */
@RestController
@RequestMapping("/api/colloqui")
@Validated
public class ColloquioTirocinioController {

    private final ColloquioTirocinioService colloquioService;

    public ColloquioTirocinioController(ColloquioTirocinioService colloquioService) {
        this.colloquioService = colloquioService;
    }

    /**
     * GET /api/colloqui - Restituisce tutti i colloqui.
     */
    @GetMapping
    public ResponseEntity<List<ColloquioTirocinioDTO>> getColloqui(
            @RequestParam(required = false) Integer allievoId,
            @RequestParam(required = false) Integer aziendaId) {

        if (allievoId != null && aziendaId != null) {
            return ResponseEntity.ok(colloquioService.findByAllievoIdAndAziendaId(allievoId, aziendaId));
        }

        if (allievoId != null) {
            return ResponseEntity.ok(colloquioService.findByAllievoId(allievoId));
        }

        if (aziendaId != null) {
            return ResponseEntity.ok(colloquioService.findByAziendaId(aziendaId));
        }

        return ResponseEntity.ok(colloquioService.findAll());
    }
    

    /**
     * GET /api/colloqui/{id} - Recupera un colloquio per ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColloquioTirocinioDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(colloquioService.findById(id));
    }

    /**
     * GET /api/colloqui?start=YYYY-MM-DD&end=YYYY-MM-DD - Filtra per periodo.
     *
     * @param start data inizio (inclusa)
     * @param end data fine (inclusa)
     */
    @GetMapping(params = {"start", "end"})
    public ResponseEntity<List<ColloquioTirocinioDTO>> findByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(colloquioService.findByPeriodo(start, end));
    }

    /**
     * POST /api/colloqui?allievoId=X&aziendaId=Y - Crea un nuovo colloquio.
     *
     * @param allievoId id dell'allievo (query param obbligatorio)
     * @param aziendaId id dell'azienda (query param obbligatorio)
     * @param body dati del colloquio (dataColloquio obbligatoria)
     */
    @PostMapping
    public ResponseEntity<ColloquioTirocinioDTO> create(
            @RequestParam Integer allievoId,
            @RequestParam Integer aziendaId,
            @Valid @RequestBody ColloquioTirocinioDTO body) {
        ColloquioTirocinio request = new ColloquioTirocinio();
        request.setDataColloquio(body.getDataColloquio());
        request.setTipoEvento(body.getTipoEvento());
        request.setEsito(body.getEsito());
        request.setNoteFeedback(body.getNoteFeedback());

        ColloquioTirocinioDTO creato = colloquioService.create(allievoId, aziendaId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
            .body(creato);
    }

    /**
     * PUT /api/colloqui/{id} - Aggiorna un colloquio esistente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColloquioTirocinioDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ColloquioTirocinioDTO body) {
        ColloquioTirocinio request = new ColloquioTirocinio();
        request.setDataColloquio(body.getDataColloquio());
        request.setTipoEvento(body.getTipoEvento());
        request.setEsito(body.getEsito());
        request.setNoteFeedback(body.getNoteFeedback());

        return ResponseEntity.ok(colloquioService.update(id, request));
    }

    /**
     * DELETE /api/colloqui/{id} - Elimina un colloquio.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        colloquioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
