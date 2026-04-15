package com.its.gestionale.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.its.gestionale.dto.auth.ContattoAziendaleDTO;
import com.its.gestionale.service.ContattoAziendaleService;

import jakarta.validation.Valid;

/**
 * Controller REST per la gestione dei contatti aziendali.
 */
@RestController
@RequestMapping("/api/contatti")
@Validated
public class ContattoAziendaleController {

    private final ContattoAziendaleService contattoService;

    public ContattoAziendaleController(ContattoAziendaleService contattoService) {
        this.contattoService = contattoService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ContattoAziendaleDTO> create(
            @Valid @RequestBody ContattoAziendaleDTO dto) {

        ContattoAziendaleDTO salvato = contattoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    // GET per azienda
    @GetMapping("/azienda/{aziendaId}")
    public ResponseEntity<List<ContattoAziendaleDTO>> findByAziendaId(
            @PathVariable Integer aziendaId) {

        return ResponseEntity.ok(contattoService.findByAziendaId(aziendaId));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        contattoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}