package com.its.gestionale.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.its.gestionale.dto.DocumentoTirocinioDTO;
import com.its.gestionale.entity.DocumentoTirocinio;
import com.its.gestionale.service.DocumentoTirocinioService;

/**
 * Controller REST per la gestione dei documenti associati ai tirocini.
 *
 * Espone endpoint annidati sotto /api/tirocini/{id}/documenti per operazioni CRUD
 * sui documenti collegati a uno specifico tirocinio.
 *
 * Mantiene il controller leggero delegando la logica al service layer.
 */

@RestController
@RequestMapping("/api/tirocini/{id}/documenti")
public class DocumentoTirocinioController {

    private final DocumentoTirocinioService service;

    public DocumentoTirocinioController(DocumentoTirocinioService service) {
        this.service = service;
    }

    /**
 * Recupera tutti i documenti associati a un tirocinio.
 *
 * @param id identificativo del tirocinio
 * @return lista di documenti collegati
 */
    @GetMapping
    public ResponseEntity<List<DocumentoTirocinioDTO>> findAll(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findByTirocinioId(id));
    }

    /**
    * Crea un nuovo documento e lo associa al tirocinio specificato.
    *
    * Il metodo riceve un DTO dal client, lo converte in entity e delega al service
    * la logica di persistenza e associazione con il tirocinio (many-to-one).
    *
    * @param id identificativo del tirocinio a cui associare il documento   
    * @param body dati del documento (tipo, presenza, data acquisizione)
    * @return documento creato con ID generato
    */
    @PostMapping
    public ResponseEntity<DocumentoTirocinioDTO> create(
            @PathVariable Integer id,
            @RequestBody DocumentoTirocinioDTO body) {

        DocumentoTirocinio doc = new DocumentoTirocinio();
        doc.setTipoDocumento(body.getTipoDocumento());
        doc.setPresente(body.getPresente());
        doc.setDataAcquisizione(body.getDataAcquisizione());

        DocumentoTirocinioDTO created = service.create(id, doc);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Elimina un documento specifico.
     *
     * @param docId identificativo del documento
     */
    @DeleteMapping("/{docId}")
    public ResponseEntity<Void> delete(@PathVariable Integer docId) {
        service.delete(docId);
        return ResponseEntity.noContent().build();
    }
}