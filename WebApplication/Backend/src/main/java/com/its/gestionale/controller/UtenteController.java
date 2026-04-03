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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.dto.UtenteDTO;
import com.its.gestionale.service.UtenteService;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // GET /api/utenti
    @GetMapping
    public ResponseEntity<List<UtenteDTO>> findAll() {
        return ResponseEntity.ok(utenteService.findAll());
    }

    // GET /api/utenti/email?email=...
    @GetMapping("/email")
    public ResponseEntity<UtenteDTO> findByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(utenteService.findByEmail(email));
    }

    // POST /api/utenti
    @PostMapping
    public ResponseEntity<UtenteDTO> create(@RequestBody UtenteDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(utenteService.save(dto));
    }

    // PUT /api/utenti/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UtenteDTO> update(
            @PathVariable Integer id,
            @RequestBody UtenteDTO dto) {
        return ResponseEntity.ok(utenteService.update(id, dto));
    }

    // DELETE /api/utenti/{id}
    // ↑ Soft delete — l'utente rimane nel DB ma inattivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        utenteService.disattivaUtente(id);
        return ResponseEntity.noContent().build();
    }
}
