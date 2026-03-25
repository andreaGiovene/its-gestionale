package com.its.gestionale.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.entity.Corso;
import com.its.gestionale.service.CorsoService;

@RestController
// ↑ Dice a Spring: "questa classe gestisce richieste HTTP
//   e restituisce dati JSON automaticamente"

@RequestMapping("/api/corsi")
// ↑ Tutte le API di questa classe iniziano con /api/corsi
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
    public ResponseEntity<List<Corso>> findAll() {
        List<Corso> corsi = corsoService.findAll();
        return ResponseEntity.ok(corsi);
        // ResponseEntity.ok() = risposta HTTP 200 con il body
    }

    // ─────────────────────────────────────────
    // GET /api/corsi/1
    // Restituisce il corso con id=1
    // ─────────────────────────────────────────
    @GetMapping("/{id}")
    // ↑ {id} è una variabile nel percorso URL
    public ResponseEntity<Corso> findById(@PathVariable Long id) {
        // @PathVariable prende il valore {id} dall'URL
        return corsoService.findById(id)
                .map(ResponseEntity::ok)
                // ↑ Se trovato → risposta 200 con il corso
                .orElse(ResponseEntity.notFound().build());
                // ↑ Se non trovato → risposta 404
    }

    // ─────────────────────────────────────────
    // POST /api/corsi
    // Crea un nuovo corso
    // Body della richiesta (JSON):
    // { "nome": "Corso X", "annoAccademico": "2025/2026", "stato": "ATTIVO" }
    // ─────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Corso> create(@RequestBody Corso corso) {
        // @RequestBody converte automaticamente il JSON
        // ricevuto in un oggetto Corso Java
        Corso salvato = corsoService.save(corso);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
        // ↑ Risposta HTTP 201 (Created) con il corso appena creato
        //   (incluso l'id generato dal DB)
    }

    // ─────────────────────────────────────────
    // PUT /api/corsi/1
    // Aggiorna il corso con id=1
    // ─────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Corso> update(
            @PathVariable Long id,
            @RequestBody Corso corso) {

        return corsoService.findById(id)
                .map(corsoEsistente -> {
                    // Aggiorna i campi del corso esistente
                    corsoEsistente.setNome(corso.getNome());
                    corsoEsistente.setAnnoAccademico(corso.getAnnoAccademico());
                    corsoEsistente.setStato(corso.getStato());
                    // Salva e restituisce il corso aggiornato
                    return ResponseEntity.ok(corsoService.save(corsoEsistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────
    // DELETE /api/corsi/1
    // Elimina il corso con id=1
    // ─────────────────────────────────────────
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!corsoService.findById(id).isPresent()) {
        return ResponseEntity.notFound().build();
        // ↑ 404 se il corso non esiste
    }
    corsoService.deleteById(id);
    return ResponseEntity.noContent().build();
    // ↑ 204 No Content — eliminato con successo
}
}