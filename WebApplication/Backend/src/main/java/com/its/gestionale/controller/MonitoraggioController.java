package com.its.gestionale.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.its.gestionale.dto.MonitoraggioDTO;
import com.its.gestionale.entity.Monitoraggio;
import com.its.gestionale.service.MonitoraggioService;

import jakarta.validation.Valid;

/**
 * Controller REST per la gestione dei monitoraggi.
 *
 * Espone endpoint CRUD per la gestione dei monitoraggi associati ai tirocini.
 * Mantiene il controller leggero delegando la logica al service layer.
 */
@RestController
@RequestMapping("/api/monitoraggi")
@Validated
public class MonitoraggioController {

    private final MonitoraggioService service;

    public MonitoraggioController(MonitoraggioService service) {
        this.service = service;
    }

    /**
     * GET ALL per i monitoraggi.
     * Restituisce la lista di tutti i monitoraggi.
     * Supporta anche il filtro per tirocinioId tramite query parameter.
     */
    @GetMapping
    public ResponseEntity<List<MonitoraggioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * GET BY ID per i monitoraggi.
     * Restituisce il monitoraggio con l'id specificato.
     * @param id id del monitoraggio da recuperare
     * @return ResponseEntity con il monitoraggio
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoraggioDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // 🔹 GET BY TIROCINIO (filtro)
    /**
     * GET BY TIROCINIO per i monitoraggi.
     * Restituisce la lista di monitoraggi associati a un tirocinio.
     * @param tirocinioId id del tirocinio
     * @return ResponseEntity con la lista di monitoraggi
     */

    @GetMapping(params = "tirocinioId")
    public ResponseEntity<List<MonitoraggioDTO>> findByTirocinioId(
            @RequestParam Integer tirocinioId) {
        return ResponseEntity.ok(service.findByTirocinioId(tirocinioId));
    }

    /**
     * CREATE per i monitoraggi.
     * Crea un nuovo monitoraggio associato a un tirocinio.
     * @param tirocinioId id del tirocinio
     * @param body body del request con i dati del monitoraggio
     * @return ResponseEntity con il monitoraggio creato
     */
    @PostMapping
    public ResponseEntity<MonitoraggioDTO> create(
            @RequestParam Integer tirocinioId,
            @Valid @RequestBody MonitoraggioDTO body) {

        Monitoraggio m = new Monitoraggio();
        m.setDataMonitoraggio(body.getDataMonitoraggio());
        m.setResponsabileId(body.getResponsabileId());
        m.setNote(body.getNote());

        MonitoraggioDTO created = service.create(tirocinioId, m);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    /**
     * UPDATE per i monitoraggi.
     * Aggiorna un monitoraggio esistente.
     * @param id id del monitoraggio da aggiornare
     * @param body body del request con i dati del monitoraggio
     * @return ResponseEntity con il monitoraggio aggiornato
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoraggioDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody MonitoraggioDTO body) {

        Monitoraggio m = new Monitoraggio();
        m.setDataMonitoraggio(body.getDataMonitoraggio());
        m.setResponsabileId(body.getResponsabileId());
        m.setNote(body.getNote());

        return ResponseEntity.ok(service.update(id, m));
    }

    /**
     * DELETE per i monitoraggi.
     * Elimina un monitoraggio esistente.
     * @param id id del monitoraggio da eliminare
     * @return ResponseEntity con no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET PER CAP per i monitoraggi.
     * Recupera i monitoraggi ordinati per CAP azienda
     * per ottimizzare la pianificazione delle visite.
     */
    @GetMapping("/pianifica")
    public ResponseEntity<List<MonitoraggioDTO>> pianifica() {
        return ResponseEntity.ok(service.pianificaPerCap());
    }

}