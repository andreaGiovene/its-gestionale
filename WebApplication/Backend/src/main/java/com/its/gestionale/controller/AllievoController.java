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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.dto.AllievoDTO;
import com.its.gestionale.service.AllievoService;

@RestController
@RequestMapping("/api/allievi")
public class AllievoController {

    private final AllievoService allievoService;

    public AllievoController(AllievoService allievoService) {
        this.allievoService = allievoService;
    }

    // GET /api/allievi
    @GetMapping
    public ResponseEntity<List<AllievoDTO>> findAll() {
        return ResponseEntity.ok(allievoService.findAll());
    }

    // GET /api/allievi?corsoId=1
    // ↑ Parametro opzionale — filtra per corso
    @GetMapping(params = "corsoId")
    public ResponseEntity<List<AllievoDTO>> findByCorso(
            @RequestParam Integer corsoId) {
        // @RequestParam prende il valore dal query string
        // es. /api/allievi?corsoId=1
        return ResponseEntity.ok(allievoService.findByCorsoId(corsoId));
    }

    // GET /api/allievi/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AllievoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(allievoService.findById(id));
    }

    // POST /api/allievi
    @PostMapping
    public ResponseEntity<AllievoDTO> create(@RequestBody AllievoDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(allievoService.save(dto));
    }

    // PUT /api/allievi/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AllievoDTO> update(
            @PathVariable Integer id,
            @RequestBody AllievoDTO dto) {
        return ResponseEntity.ok(allievoService.update(id, dto));
    }

    // DELETE /api/allievi/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        allievoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}