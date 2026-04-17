package com.its.gestionale.controller;

import com.its.gestionale.service.AziendaImportService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller REST per l'importazione delle aziende da file Excel.
 *
 * Espone endpoint per upload file e delega la logica al service.
 */
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class AziendaImportController {

    private final AziendaImportService aziendaImportService;

    /**
     * Endpoint per importare aziende da file Excel (.xlsx)
     *
     * @param file file Excel caricato
     * @return messaggio di conferma
     */
    @PostMapping("/aziende")
    public ResponseEntity<String> importAziende(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File vuoto");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("File non valido. Carica un file .xlsx");
        }

        aziendaImportService.importAziende(file);

        return ResponseEntity.ok("Import aziende completato con successo");
    }
}