package com.its.gestionale.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.its.gestionale.dto.AziendaDTO;
import com.its.gestionale.entity.enums.TipoAzienda;
import com.its.gestionale.service.AziendaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/aziende")
@Validated
public class AziendaController {

    private final AziendaService aziendaService;

    public AziendaController(AziendaService aziendaService) {
        this.aziendaService = aziendaService;
    }

    @GetMapping
    public ResponseEntity<Page<AziendaDTO>> search(
            @RequestParam(required = false) TipoAzienda tipo,
            @RequestParam(required = false) @Size(max = 100) String ragioneSociale,
            @RequestParam(required = false) @Positive Integer corsoId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(aziendaService.search(tipo, ragioneSociale, corsoId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AziendaDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(aziendaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AziendaDTO> create(@Valid @RequestBody AziendaDTO aziendaDTO) {
        AziendaDTO salvata = aziendaService.create(aziendaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvata);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AziendaDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody AziendaDTO aziendaDTO) {
        return ResponseEntity.ok(aziendaService.update(id, aziendaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        aziendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
