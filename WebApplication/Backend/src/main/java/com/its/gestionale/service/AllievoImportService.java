package com.its.gestionale.service;

import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Corso;
import com.its.gestionale.repository.AllievoRepository;
import com.its.gestionale.repository.CorsoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service per l'importazione degli allievi da file Excel (.xlsx).
 */
@Service
@RequiredArgsConstructor
public class AllievoImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllievoImportService.class);

    private static final List<String> REQUIRED_HEADER_KEYS = List.of(
        "nome",
        "cognome",
        "codicefiscale"
    );

    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
        DateTimeFormatter.ofPattern("dd/MM/uuuu"),
        DateTimeFormatter.ofPattern("d/M/uuuu"),
        DateTimeFormatter.ofPattern("dd-MM-uuuu"),
        DateTimeFormatter.ofPattern("d-M-uuuu"),
        DateTimeFormatter.ISO_LOCAL_DATE
    );

    private final AllievoRepository allievoRepository;
    private final CorsoRepository corsoRepository;

    public void importAllievi(MultipartFile file) {
        int count = 0;

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = findHeaderRow(sheet);

            if (headerRow == null) {
                throw new IllegalArgumentException("Impossibile trovare la riga di intestazione del file Excel allievi");
            }

            Map<String, Integer> headerIndex = buildHeaderIndex(headerRow);
            if (!hasRequiredHeaders(headerIndex)) {
                throw new IllegalArgumentException("Il file Excel allievi deve contenere le intestazioni Nome, Cognome e Codice Fiscale");
            }

            for (int rowIndex = headerRow.getRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                String nome = getCellValueByHeader(row, headerIndex, "nome", "nomeallievo");
                String cognome = getCellValueByHeader(row, headerIndex, "cognome", "cognomeallievo");
                String codiceFiscale = getCellValueByHeader(row, headerIndex, "codicefiscale", "codfiscale", "cf");
                String corsoIdStr = getCellValueByHeader(row, headerIndex, "corsoid", "idcorso", "corso_id", "corso");
                String telefono = getCellValueByHeader(row, headerIndex, "telefono", "cellulare");
                LocalDate dataDiNascita = getDateCellValueByHeader(row, headerIndex, "datadinascita", "datanascita", "nascita");
                String note = getCellValueByHeader(row, headerIndex, "note", "annotazioni");

                if (codiceFiscale == null || codiceFiscale.isBlank()) {
                    continue;
                }

                if (allievoRepository.existsByCodiceFiscale(codiceFiscale)) {
                    continue;
                }

                Allievo allievo = new Allievo();
                allievo.setNome(nome);
                allievo.setCognome(cognome);
                allievo.setCodiceFiscale(codiceFiscale);

                Integer corsoId = parseIntegerSafe(corsoIdStr);
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
            LOGGER.error("Errore durante import Excel allievi", e);
            throw new RuntimeException("Errore durante import Excel allievi", e);
        }
    }

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

        return bestScore >= 3 ? bestRow : null;
    }

    private int scoreHeaderRow(Row row) {
        int score = 0;

        for (Cell cell : row) {
            String normalizedHeader = normalizeHeader(getCellValue(cell));
            if (REQUIRED_HEADER_KEYS.contains(normalizedHeader)
                || normalizedHeader.equals("telefono")
                || normalizedHeader.equals("cellulare")
                || normalizedHeader.equals("datadinascita")
                || normalizedHeader.equals("datanascita")
                || normalizedHeader.equals("nascita")
                || normalizedHeader.equals("note")
                || normalizedHeader.equals("annotazioni")
                || normalizedHeader.equals("corsoid")
                || normalizedHeader.equals("idcorso")
                || normalizedHeader.equals("corso_id")
                || normalizedHeader.equals("corso")) {
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
        return headerIndex.containsKey(normalizeHeader("nome"))
            && headerIndex.containsKey(normalizeHeader("cognome"))
            && (headerIndex.containsKey(normalizeHeader("codice fiscale"))
                || headerIndex.containsKey(normalizeHeader("codicefiscale"))
                || headerIndex.containsKey(normalizeHeader("codfiscale"))
                || headerIndex.containsKey(normalizeHeader("cf")));
    }

    private String getCellValueByHeader(Row row, Map<String, Integer> headerIndex, String... aliases) {
        Cell cell = resolveCellByHeader(row, headerIndex, aliases);
        return getCellValue(cell);
    }

    private LocalDate getDateCellValueByHeader(Row row, Map<String, Integer> headerIndex, String... aliases) {
        Cell cell = resolveCellByHeader(row, headerIndex, aliases);
        return getDateCellValue(cell);
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
            .toLowerCase()
            .replaceAll("[^a-z0-9]", "")
            .trim();
    }

    private Integer parseIntegerSafe(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private LocalDate getDateCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        if (cell.getCellType() == CellType.STRING) {
            String rawValue = cell.getStringCellValue();
            if (rawValue == null || rawValue.isBlank()) {
                return null;
            }

            String normalizedValue = rawValue.trim();
            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(normalizedValue, formatter);
                } catch (DateTimeParseException ignored) {
                    // Prova il formato successivo.
                }
            }
        }

        return null;
    }
}