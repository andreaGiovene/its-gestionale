package com.its.gestionale.controller;

import java.util.List;

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
import com.its.gestionale.dto.ContattoDTO;
import com.its.gestionale.entity.enums.TipoAzienda;
import com.its.gestionale.service.AziendaService;
import com.its.gestionale.service.ContattoAziendaleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * REST Controller per la gestione delle aziende.
 *
 * Espone l'API REST per operazioni CRUD e ricerca avanzata su aziende,
 * delegando la logica di business al service layer (AziendaService).
 *
 * Endpoint disponibili:
 * - GET /api/aziende - ricerca paginata con filtri opzionali (tipo, ragioneSociale, corsoId)
 * - GET /api/aziende/{id} - recupero singolo per ID
 * - GET /api/aziende/{id}/contatti - recupero contatti associati ad un'azienda
 * - POST /api/aziende/{id}/contatti - creazione di un contatto per un'azienda
 * - POST /api/aziende - creazione nuova azienda
 * - PUT /api/aziende/{id} - aggiornamento azienda esistente
 * - DELETE /api/aziende/{id} - eliminazione azienda
 *
 * Validazioni applicate:
 * - Path variable: ids positivi
 * - RequestParam: ragioneSociale max 100 char, corsoId positivo
 * - RequestBody: validazioni dal DTO (@Valid)
 *
 * HTTP Status restituiti:
 * - 200 OK: operazione completata
 * - 201 CREATED: risorsa creata con POST
 * - 204 NO_CONTENT: eliminazione riuscita
 * - 400 BAD_REQUEST: validazione fallita
 * - 404 NOT_FOUND: risorsa non trovata
 *
 * @see AziendaService
 * @see ContattoAziendaleService
 * @see AziendaDTO
 */
@RestController
@RequestMapping("/api/aziende")
@Validated
public class AziendaController {

    private final AziendaService aziendaService;
    private final ContattoAziendaleService contattoAziendaleService;

    public AziendaController(AziendaService aziendaService, ContattoAziendaleService contattoAziendaleService) {
        this.aziendaService = aziendaService;
        this.contattoAziendaleService = contattoAziendaleService;
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

    @GetMapping("/{id}/contatti")
    public ResponseEntity<List<ContattoDTO>> getContatti(@PathVariable @Positive Integer id) {
        // Verifica che l'azienda esista prima di recuperare i contatti
        aziendaService.findById(id);
        List<ContattoDTO> contatti = contattoAziendaleService.findByAziendaIdAsDto(id);
        return ResponseEntity.ok(contatti);
    }

    @PostMapping("/{id}/contatti")
    public ResponseEntity<ContattoDTO> createContatto(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ContattoDTO contattoDTO) {
        ContattoDTO salvato = contattoAziendaleService.createForAzienda(id, contattoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
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
