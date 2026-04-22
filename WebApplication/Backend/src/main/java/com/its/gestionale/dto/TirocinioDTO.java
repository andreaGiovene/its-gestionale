package com.its.gestionale.dto;

import java.time.LocalDate;

import com.its.gestionale.entity.Tirocinio;
import com.its.gestionale.entity.enums.StatoTirocinio;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TirocinioDTO {

    private Integer id;

    @NotNull(message = "La data di inizio è obbligatoria")
    private LocalDate dataInizio;

    private LocalDate dataFine;

    @Size(max = 50, message = "Il tipo non può superare 50 caratteri")
    private String tipo;

    @Size(max = 50, message = "La frequenza non può superare 50 caratteri")
    private String frequenza;

    @NotNull(message = "L'esito è obbligatorio")
    private StatoTirocinio esito;

    @NotNull(message = "L'allievo è obbligatorio")
    private Integer allievoId;

    @NotNull(message = "L'azienda è obbligatoria")
    private Integer aziendaId;

    // 🔹 MAPPING
    public static TirocinioDTO fromEntity(Tirocinio t) {
        TirocinioDTO dto = new TirocinioDTO();

        dto.setId(t.getId());
        dto.setDataInizio(t.getDataInizio());
        dto.setDataFine(t.getDataFine());
        dto.setTipo(t.getTipo());
        dto.setFrequenza(t.getFrequenza());
        dto.setEsito(t.getEsito());

        if (t.getAllievo() != null) {
            dto.setAllievoId(t.getAllievo().getId());
        }

        if (t.getAzienda() != null) {
            dto.setAziendaId(t.getAzienda().getId());
        }

        return dto;
    }
}