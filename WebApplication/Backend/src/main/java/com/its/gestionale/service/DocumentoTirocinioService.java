package com.its.gestionale.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.DocumentoTirocinioDTO;
import com.its.gestionale.entity.DocumentoTirocinio;
import com.its.gestionale.entity.Tirocinio;
import com.its.gestionale.repository.DocumentoTirocinioRepository;
import com.its.gestionale.repository.TirocinioRepository;

/**
 * Service applicativo per la gestione dei documenti di tirocinio.
 *
 * Gestisce la logica di business e la relazione tra DocumentoTirocinio e Tirocinio,
 * mantenendo i controller privi di logica complessa.
 */

@Service
public class DocumentoTirocinioService {

    private final DocumentoTirocinioRepository repo;
    private final TirocinioRepository tirocinioRepo;

    public DocumentoTirocinioService(DocumentoTirocinioRepository repo,
                                     TirocinioRepository tirocinioRepo) {
        this.repo = repo;
        this.tirocinioRepo = tirocinioRepo;
    }

    /**
     * Recupera i documenti associati a un tirocinio.
     *
     * Verifica l'esistenza del tirocinio prima di eseguire la query.
     *
     * @param tirocinioId identificativo del tirocinio
     * @return lista di documenti
     */
    @Transactional(readOnly = true)
    public List<DocumentoTirocinioDTO> findByTirocinioId(Integer tirocinioId) {

        if (!tirocinioRepo.existsById(tirocinioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tirocinio non trovato");
        }

        return repo.findByTirocinioId(tirocinioId)
                .stream()
                .map(DocumentoTirocinioDTO::fromEntity)
                .toList();
    }

    /**
     * Crea un documento associandolo a un tirocinio esistente.
     *
     * Imposta la relazione many-to-one tra documento e tirocinio.
     *
     * @param tirocinioId identificativo del tirocinio
     * @param doc entity documento da salvare
     * @return documento salvato
     */
    @Transactional
    public DocumentoTirocinioDTO create(Integer tirocinioId, DocumentoTirocinio doc) {

        Tirocinio tirocinio = tirocinioRepo.findById(tirocinioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tirocinio non trovato"));

        doc.setTirocinio(tirocinio);

        return DocumentoTirocinioDTO.fromEntity(repo.save(doc));
    }

    /**
     * Elimina un documento dal sistema.
     *
     * @param docId identificativo del documento
     */
    @Transactional
    public void delete(Integer docId) {

        if (!repo.existsById(docId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento non trovato");
        }

        repo.deleteById(docId);
    }
}