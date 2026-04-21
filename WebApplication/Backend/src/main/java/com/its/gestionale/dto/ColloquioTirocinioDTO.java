package com.its.gestionale.dto;

import java.time.LocalDate;

import com.its.gestionale.entity.ColloquioTirocinio;
import com.its.gestionale.entity.enums.StatoEsitoColloquio;
import com.its.gestionale.entity.enums.TipoEventoColloquio;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per il colloquio di tirocinio.
 *
 * Espone i dati del colloquio senza propagare l'intera entity verso il client.
 * Include informazioni essenziali dell'allievo e dell'azienda tramite ID e nomi.
 */
@Data
@NoArgsConstructor
public class ColloquioTirocinioDTO {

    private Integer id;
    private LocalDate dataColloquio;
    private TipoEventoColloquio tipoEvento;
    private StatoEsitoColloquio esito;
    private String noteFeedback;

    // Informazioni allievo (essenziali)
    private Integer allievoId;
    private String allievoNome;
    private String allievano;

    // Informazioni azienda (essenziali)
    private Integer aziendaId;
    private String aziendaRagioneSociale;

    /**
     * Converte un'entity ColloquioTirocinio in DTO.
     *
     * Centralizza il mapping in un metodo statico per mantenere coerenza
     * tra service e controller.
     */
    public static ColloquioTirocinioDTO fromEntity(ColloquioTirocinio entity) {
        ColloquioTirocinioDTO dto = new ColloquioTirocinioDTO();

        dto.setId(entity.getId());
        dto.setDataColloquio(entity.getDataColloquio());
        dto.setTipoEvento(entity.getTipoEvento());
        dto.setEsito(entity.getEsito());
        dto.setNoteFeedback(entity.getNoteFeedback());

        if (entity.getAllievo() != null) {
            dto.setAllievoId(entity.getAllievo().getId());
            dto.setAllievoNome(entity.getAllievo().getNome());
            dto.setAllievano(entity.getAllievo().getCognome());
        }

        if (entity.getAzienda() != null) {
            dto.setAziendaId(entity.getAzienda().getId());
            dto.setAziendaRagioneSociale(entity.getAzienda().getRagioneSociale());
        }

        return dto;
    }
}
