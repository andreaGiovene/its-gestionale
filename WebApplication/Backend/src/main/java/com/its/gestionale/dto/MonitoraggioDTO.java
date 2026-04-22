package com.its.gestionale.dto;

import java.time.LocalDate;

import com.its.gestionale.entity.Monitoraggio;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object per Monitoraggio.
 *
 * Rappresenta i dati esposti tramite API REST per i monitoraggi,
 * evitando di esporre direttamente l'entity JPA.
 *
 * Include il riferimento al tirocinio tramite tirocinioId.
 */
@Data
@NoArgsConstructor
public class MonitoraggioDTO {

    private Integer id;
    private LocalDate dataMonitoraggio;
    private Integer responsabileId;
    private String note;

    private Integer tirocinioId;

    /**
     * Converte un'entity Monitoraggio in DTO.
     *
     * @param m entity da convertire
     * @return DTO popolato con i dati dell'entity
     */
    public static MonitoraggioDTO fromEntity(Monitoraggio m) {
        MonitoraggioDTO dto = new MonitoraggioDTO();

        dto.setId(m.getId());
        dto.setDataMonitoraggio(m.getDataMonitoraggio());
        dto.setResponsabileId(m.getResponsabileId());
        dto.setNote(m.getNote());

        if (m.getTirocinio() != null) {
            dto.setTirocinioId(m.getTirocinio().getId());
        }

        return dto;
    }
}