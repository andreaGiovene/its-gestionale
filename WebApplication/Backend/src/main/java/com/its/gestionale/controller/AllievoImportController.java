package com.its.gestionale.controller;

import com.its.gestionale.service.AllievoImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**
 * Controller REST per l'importazione degli allievi da file Excel.
 *
 * Espone endpoint per upload file e delega la logica al service.
 */

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class AllievoImportController {

    private final AllievoImportService allievoImportService;

    @PostMapping("/allievi")
    public ResponseEntity<String> importAllievi(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File vuoto");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Formato file non valido");
        }

        allievoImportService.importAllievi(file);

        return ResponseEntity.ok("Import allievi completato");
    }
}