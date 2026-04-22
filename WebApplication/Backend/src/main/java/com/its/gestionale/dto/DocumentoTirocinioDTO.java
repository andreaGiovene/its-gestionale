package com.its.gestionale.dto;

import java.time.LocalDate;

import com.its.gestionale.entity.DocumentoTirocinio;
import com.its.gestionale.entity.enums.TipoDocumento;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object per DocumentoTirocinio.
 *
 * Rappresenta i dati esposti tramite API REST per i documenti di tirocinio,
 * evitando di esporre direttamente l'entity JPA.
 *
 * Contiene le informazioni principali del documento e il riferimento al tirocinio
 * tramite tirocinioId.
 *
 * Utilizzato per:
 * - invio dati dal backend al client (response)
 * - ricezione dati dal client (request)
 */

@Data
@NoArgsConstructor
public class DocumentoTirocinioDTO {

    private Integer id;
    private TipoDocumento tipoDocumento;
    private Boolean presente;
    private LocalDate dataAcquisizione;
    private Integer tirocinioId;

    public static DocumentoTirocinioDTO fromEntity(DocumentoTirocinio doc) {
        DocumentoTirocinioDTO dto = new DocumentoTirocinioDTO();

        dto.setId(doc.getId());
        dto.setTipoDocumento(doc.getTipoDocumento());
        dto.setPresente(doc.getPresente());
        dto.setDataAcquisizione(doc.getDataAcquisizione());

        if (doc.getTirocinio() != null) {
            dto.setTirocinioId(doc.getTirocinio().getId());
        }

        return dto;
    }
}