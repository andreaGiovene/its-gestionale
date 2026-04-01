package com.its.gestionale.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.dto.CorsoDTO;
import com.its.gestionale.service.CorsoService;

import jakarta.validation.Valid;

@RestController
// ↑ Dice a Spring: "questa classe gestisce richieste HTTP
//   e restituisce dati JSON automaticamente"

@RequestMapping("/api/corsi")
// ↑ Tutte le API di questa classe iniziano con /api/corsi
@Validated
public class CorsoController {

    private final CorsoService corsoService;

    public CorsoController(CorsoService corsoService) {
        this.corsoService = corsoService;
    }

    // ─────────────────────────────────────────
    // GET /api/corsi
    // Restituisce tutti i corsi
    // ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<CorsoDTO>> findAll() {
        return ResponseEntity.ok(corsoService.findAll());
    }

    // ─────────────────────────────────────────
    // GET /api/corsi/1
    // Restituisce il corso con id=1
    // ─────────────────────────────────────────
    @GetMapping("/{id}")
    // ↑ {id} è una variabile nel percorso URL
    public ResponseEntity<CorsoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(corsoService.findById(id));
    }

    // ─────────────────────────────────────────
    // POST /api/corsi
    // Crea un nuovo corso
    // Body della richiesta (JSON):
    // { "nome": "Corso X", "annoAccademico": "2025/2026", "stato": "ATTIVO" }
    // ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<CorsoDTO> create(@Valid @RequestBody CorsoDTO corsoDTO) {
        CorsoDTO salvato = corsoService.create(corsoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    // ─────────────────────────────────────────
    // PUT /api/corsi/1
    // Aggiorna il corso con id=1
    // ─────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<CorsoDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody CorsoDTO corsoDTO) {
        return ResponseEntity.ok(corsoService.update(id, corsoDTO));
    }

    // ─────────────────────────────────────────
    // DELETE /api/corsi/1
    // Elimina il corso con id=1
    // ─────────────────────────────────────────
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Integer id) {
    corsoService.deleteById(id);
    return ResponseEntity.noContent().build();
    // ↑ 204 No Content — eliminato con successo
}
}