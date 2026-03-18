package com.its.gestionale.controller;

import com.its.gestionale.dto.AllievoDTO;
import com.its.gestionale.service.AllievoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allievi")
@CrossOrigin(origins = "*")
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
            @RequestParam Long corsoId) {
        // @RequestParam prende il valore dal query string
        // es. /api/allievi?corsoId=1
        return ResponseEntity.ok(allievoService.findByCorsoId(corsoId));
    }

    // POST /api/allievi
    @PostMapping
    public ResponseEntity<AllievoDTO> create(@RequestBody AllievoDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(allievoService.save(dto));
    }
}