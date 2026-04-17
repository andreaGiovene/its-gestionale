package com.its.gestionale.service;

import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.enums.TipoAzienda;
import com.its.gestionale.repository.AziendaRepository;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

/**
 * Service per l'importazione delle aziende da file Excel (.xlsx).
 *
 * Responsabilità:
 * - leggere il file Exel 
 * - convertire ogni riga in entity Azienda
 * - evitare duplicati (partita IVA)
 * - salvare nel database
 */
@Service
@RequiredArgsConstructor
public class AziendaImportService {

    private final AziendaRepository aziendaRepository;

    /**
     * Metodo principale per import Excel.
     *
     * @param file file Excel caricato
     */
    public void importAziende(MultipartFile file) {

        int count = 0; // contatore aziende importate

        try (InputStream is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Salta intestazione (prima riga)
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();

                // Lettura celle
                String ragioneSociale = getCellValue(row.getCell(0));
                //String partitaIva = getCellValue(row.getCell(1));
                String email = getCellValue(row.getCell(2));
                //String telefono = getCellValue(row.getCell(3));
                String indirizzo = getCellValue(row.getCell(3));
                //String cap = getCellValue(row.getCell(5));
                //String citta = getCellValue(row.getCell(6));

                // 🔹 controllo partita IVA valida
                //if (partitaIva == null || partitaIva.isBlank()) {
                //    continue;
                //}

                if (ragioneSociale == null || ragioneSociale.isBlank()) {
                    continue;
                }

                if (aziendaRepository.existsByRagioneSociale(ragioneSociale)) {
                    continue;
                }

                Azienda azienda = new Azienda();
                azienda.setRagioneSociale(ragioneSociale);
                azienda.setEmail(email);
                //azienda.setPartitaIva(partitaIva);
                //azienda.setEmail(email);
                //azienda.setTelefono(telefono);
                azienda.setIndirizzo(indirizzo);
                //azienda.setCap(cap);
                //azienda.setCitta(citta);

                azienda.setTipo(TipoAzienda.NON_MADRINA);

                aziendaRepository.save(azienda);
                count++;
            }

            // 🔹 log finale (utile per debug e tesi)
            System.out.println("Import completato. Aziende salvate: " + count);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante import Excel aziende", e);
        }
    }

    /**
     * Metodo di utilità per leggere il valore di una cella in modo sicuro.
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }
}