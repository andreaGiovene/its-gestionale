package com.its.gestionale.service;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.enums.TipoAzienda;
import com.its.gestionale.repository.AziendaRepository;

import lombok.RequiredArgsConstructor;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(AziendaImportService.class);

    private static final List<String> REQUIRED_HEADER_KEYS = List.of(
        "ragionesociale",
        "partitaiva"
    );

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
            Row headerRow = findHeaderRow(sheet);

            if (headerRow == null) {
                throw new IllegalArgumentException("Impossibile trovare la riga di intestazione del file Excel aziende");
            }

            Map<String, Integer> headerIndex = buildHeaderIndex(headerRow);
            if (!hasRequiredHeaders(headerIndex)) {
                throw new IllegalArgumentException("Il file Excel aziende deve contenere le intestazioni Ragione Sociale e Partita IVA");
            }

            for (int rowIndex = headerRow.getRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                String ragioneSociale = getCellValueByHeader(row, headerIndex,
                    "ragionesociale", "ragione_sociale", "azienda", "nomeazienda", "denominazione");
                String partitaIva = getCellValueByHeader(row, headerIndex,
                    "partitaiva", "piva", "partita_iva", "vat", "vatnumber");
                String email = getCellValueByHeader(row, headerIndex,
                    "email", "mail", "postaelettronica");
                String telefono = getCellValueByHeader(row, headerIndex,
                    "telefono", "tel", "cellulare", "contatto");
                String indirizzo = getCellValueByHeader(row, headerIndex,
                    "indirizzo", "via", "sede", "sedelegale");
                String cap = getCellValueByHeader(row, headerIndex,
                    "cap", "codicepostale", "zip");
                String citta = getCellValueByHeader(row, headerIndex,
                    "citta", "comune", "city");
                String tipoValue = getCellValueByHeader(row, headerIndex,
                    "tipo", "tipoazienda", "madrina");

                //  Controllo partita IVA valida
                if (partitaIva == null || partitaIva.isBlank()) {
                    continue;
                }

                if (ragioneSociale == null || ragioneSociale.isBlank()) {
                    continue;
                }

                if (aziendaRepository.findByPartitaIva(partitaIva).isPresent()
                    || aziendaRepository.existsByRagioneSociale(ragioneSociale)) {
                    continue;
                }

                Azienda azienda = new Azienda();
                azienda.setRagioneSociale(ragioneSociale);
                azienda.setPartitaIva(partitaIva);
                azienda.setEmail(email);
                azienda.setTelefono(telefono);
                azienda.setIndirizzo(indirizzo);
                azienda.setCap(cap);
                azienda.setCitta(citta);

                TipoAzienda tipo = parseTipoAzienda(tipoValue);
                azienda.setTipo(tipo != null ? tipo : TipoAzienda.NON_MADRINA);

                aziendaRepository.save(azienda);
                count++;
            }

            // 🔹 log finale (utile per debug e tesi)
            LOGGER.info("Import completato. Aziende salvate: {}", count);

        } catch (Exception e) {
            LOGGER.error("Errore durante import Excel aziende", e);
            throw new RuntimeException("Errore durante import Excel aziende", e);
        }
    }

    /**
     * Metodo di utilità per leggere il valore di una cella in modo sicuro.
     */
    private Row findHeaderRow(Sheet sheet) {
        int maxRowsToInspect = Math.min(sheet.getLastRowNum(), 8);
        Row bestRow = null;
        int bestScore = 0;

        for (int rowIndex = 0; rowIndex <= maxRowsToInspect; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            int score = scoreHeaderRow(row);
            if (score > bestScore) {
                bestScore = score;
                bestRow = row;
            }
        }

        return bestScore >= 2 ? bestRow : null;
    }

    private int scoreHeaderRow(Row row) {
        int score = 0;

        for (Cell cell : row) {
            String normalizedHeader = normalizeHeader(getCellValue(cell));
            if (REQUIRED_HEADER_KEYS.contains(normalizedHeader)
                || normalizedHeader.equals("email")
                || normalizedHeader.equals("mail")
                || normalizedHeader.equals("postaelettronica")
                || normalizedHeader.equals("telefono")
                || normalizedHeader.equals("tel")
                || normalizedHeader.equals("cellulare")
                || normalizedHeader.equals("indirizzo")
                || normalizedHeader.equals("via")
                || normalizedHeader.equals("sede")
                || normalizedHeader.equals("cap")
                || normalizedHeader.equals("codicepostale")
                || normalizedHeader.equals("citta")
                || normalizedHeader.equals("comune")
                || normalizedHeader.equals("tipo")
                || normalizedHeader.equals("tipoazienda")
                || normalizedHeader.equals("madrina")) {
                score++;
            }
        }

        return score;
    }

    private Map<String, Integer> buildHeaderIndex(Row headerRow) {
        Map<String, Integer> headerIndex = new HashMap<>();

        for (Cell cell : headerRow) {
            String normalizedHeader = normalizeHeader(getCellValue(cell));
            if (!normalizedHeader.isBlank()) {
                headerIndex.put(normalizedHeader, cell.getColumnIndex());
            }
        }

        return headerIndex;
    }

    private boolean hasRequiredHeaders(Map<String, Integer> headerIndex) {
        return (headerIndex.containsKey(normalizeHeader("ragione sociale"))
            || headerIndex.containsKey(normalizeHeader("ragionesociale"))
            || headerIndex.containsKey(normalizeHeader("denominazione")))
            && (headerIndex.containsKey(normalizeHeader("partita iva"))
                || headerIndex.containsKey(normalizeHeader("partitaiva"))
                || headerIndex.containsKey(normalizeHeader("piva")));
    }

    private String getCellValueByHeader(Row row, Map<String, Integer> headerIndex, String... aliases) {
        Cell cell = resolveCellByHeader(row, headerIndex, aliases);
        return getCellValue(cell);
    }

    private Cell resolveCellByHeader(Row row, Map<String, Integer> headerIndex, String... aliases) {
        for (String alias : aliases) {
            Integer columnIndex = headerIndex.get(normalizeHeader(alias));
            if (columnIndex != null) {
                return row.getCell(columnIndex);
            }
        }

        return null;
    }

    private boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            String value = getCellValue(cell);
            if (value != null && !value.isBlank()) {
                return false;
            }
        }

        return true;
    }

    private String normalizeHeader(String header) {
        if (header == null) {
            return "";
        }

        return Normalizer.normalize(header, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]", "")
            .trim();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell);
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private TipoAzienda parseTipoAzienda(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = normalizeHeader(value);
        if (normalized.equals("madrina")) {
            return TipoAzienda.MADRINA;
        }

        if (normalized.equals("nonmadrina")) {
            return TipoAzienda.NON_MADRINA;
        }

        return null;
    }
}