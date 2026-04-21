package com.its.gestionale.service;

import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Corso;
import com.its.gestionale.repository.AllievoRepository;
import com.its.gestionale.repository.CorsoRepository;

import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Iterator;

/**
 * Service per l'importazione degli allievi da file Excel (.xlsx).
 */
@Service
@RequiredArgsConstructor
public class AllievoImportService {

    private final AllievoRepository allievoRepository;
    private final CorsoRepository corsoRepository;

    public void importAllievi(MultipartFile file) {

        int count = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Salta intestazione
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row row = rows.next();

                // ⚠️ ADATTA QUESTI INDICI AL TUO EXCEL
                String nome = getCellValue(row.getCell(0));
                String cognome = getCellValue(row.getCell(1));
                String codiceFiscale = getCellValue(row.getCell(2));
                String corsoIdStr = getCellValue(row.getCell(3));
                Integer corsoId = null;
                if (corsoIdStr != null && !corsoIdStr.isBlank()) {
                    corsoId = Integer.valueOf(corsoIdStr);
                }   

                String telefono = getCellValue(row.getCell(4));
                LocalDate dataDiNascita = getDateCellValue(row.getCell(5));
                String note = getCellValue(row.getCell(6));

                // 🔹 validazione minima
                if (codiceFiscale == null || codiceFiscale.isBlank()) {
                    continue;
                }

                // 🔥 deduplicazione
                if (codiceFiscale == null || codiceFiscale.isBlank()) {
                    continue;
                }

                if (allievoRepository.existsByCodiceFiscale(codiceFiscale)) {
                    continue;
                }

                // ✅ creazione entity

                Allievo allievo = new Allievo();
                allievo.setNome(nome);
                allievo.setCognome(cognome);
                allievo.setCodiceFiscale(codiceFiscale);
                if (corsoId != null) {
                    Corso corso = corsoRepository.findById(corsoId).orElse(null);

                    if (corso == null) {
                        continue;
                    }
                    allievo.setCorso(corso);
                }
                allievo.setTelefono(telefono);
                allievo.setDataDiNascita(dataDiNascita);
                allievo.setNote(note);

                allievoRepository.save(allievo);
                count++;
            }

            System.out.println("Import completato. Allievi salvati: " + count);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante import Excel allievi", e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private LocalDate getDateCellValue(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        return null;
    }
}