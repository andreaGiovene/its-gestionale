package com.its.gestionale.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.its.gestionale.dto.TirocinioDTO;
import com.its.gestionale.entity.Tirocinio;
import com.its.gestionale.service.TirocinioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tirocini")
@Validated
public class TirocinioController {

    private final TirocinioService service;

    public TirocinioController(TirocinioService service) {
        this.service = service;
    }

    // 🔹 GET ALL
    @GetMapping
    public ResponseEntity<List<TirocinioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // 🔹 GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TirocinioDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // 🔹 GET BY ALLIEVO
    @GetMapping(params = "allievoId")
    public ResponseEntity<List<TirocinioDTO>> findByAllievoId(
            @RequestParam Integer allievoId) {
        return ResponseEntity.ok(service.findByAllievoId(allievoId));
    }

    // 🔹 GET BY AZIENDA
    @GetMapping(params = "aziendaId")
    public ResponseEntity<List<TirocinioDTO>> findByAziendaId(
            @RequestParam Integer aziendaId) {
        return ResponseEntity.ok(service.findByAziendaId(aziendaId));
    }

    // 🔹 GET BY PERIODO
    @GetMapping(params = {"start", "end"})
    public ResponseEntity<List<TirocinioDTO>> findByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return ResponseEntity.ok(service.findByPeriodo(start, end));
    }

    // 🔹 CREATE
    @PostMapping
    public ResponseEntity<TirocinioDTO> create(
            @RequestParam Integer allievoId,
            @RequestParam Integer aziendaId,
            @RequestBody TirocinioDTO body) {

        Tirocinio t = new Tirocinio();
        t.setDataInizio(body.getDataInizio());
        t.setDataFine(body.getDataFine());
        t.setTipo(body.getTipo());
        t.setFrequenza(body.getFrequenza());
        t.setEsito(body.getEsito());

        TirocinioDTO created = service.create(allievoId, aziendaId, t);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // 🔹 UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TirocinioDTO> update(
            @PathVariable Integer id,
            @RequestBody TirocinioDTO body) {

        Tirocinio t = new Tirocinio();
        t.setDataInizio(body.getDataInizio());
        t.setDataFine(body.getDataFine());
        t.setTipo(body.getTipo());
        t.setFrequenza(body.getFrequenza());
        t.setEsito(body.getEsito());

        return ResponseEntity.ok(service.update(id, t));
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}