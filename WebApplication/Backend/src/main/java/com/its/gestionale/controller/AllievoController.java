package com.its.gestionale.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.its.gestionale.dto.AllievoDTO;
import com.its.gestionale.service.AllievoService;

/**
 * Controller REST per la gestione degli allievi.
 *
 * Espone endpoint CRUD e filtri di ricerca.
 */
@RestController
@RequestMapping("/api/allievi")
public class AllievoController {

    private final AllievoService allievoService;

    public AllievoController(AllievoService allievoService) {
        this.allievoService = allievoService;
    }

    /**
     * GET /api/allievi
     *
     * Restituisce tutti gli allievi.
     * Se viene passato il parametro stageAssegnato=false,
     * restituisce solo gli allievi senza tirocinio.
     *
     * Esempi:
     * - /api/allievi → tutti
     * - /api/allievi?stageAssegnato=false → solo senza stage
     */
    @GetMapping
    public ResponseEntity<List<AllievoDTO>> findAll(
            @RequestParam(required = false) Boolean stageAssegnato) {

        // Se viene richiesto esplicitamente stageAssegnato=false
        if (stageAssegnato != null && !stageAssegnato) {
            return ResponseEntity.ok(allievoService.getAllieviSenzaStage());
        }

        // Altrimenti ritorna tutti gli allievi
        return ResponseEntity.ok(allievoService.findAll());
    }

    /**
     * GET /api/allievi?corsoId=1
     *
     * Filtra gli allievi per corso.
     */
    @GetMapping(params = "corsoId")
    public ResponseEntity<List<AllievoDTO>> findByCorso(
            @RequestParam Integer corsoId) {

        return ResponseEntity.ok(allievoService.findByCorsoId(corsoId));
    }

    /**
     * GET /api/allievi/{id}
     *
     * Restituisce un singolo allievo per ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllievoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(allievoService.findById(id));
    }

    /**
     * POST /api/allievi
     *
     * Crea un nuovo allievo.
     */
    @PostMapping
    public ResponseEntity<AllievoDTO> create(@RequestBody AllievoDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(allievoService.save(dto));
    }

    /**
     * PUT /api/allievi/{id}
     *
     * Aggiorna un allievo esistente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllievoDTO> update(
            @PathVariable Integer id,
            @RequestBody AllievoDTO dto) {

        return ResponseEntity.ok(allievoService.update(id, dto));
    }

    /**
     * DELETE /api/allievi/{id}
     *
     * Elimina un allievo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        allievoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}