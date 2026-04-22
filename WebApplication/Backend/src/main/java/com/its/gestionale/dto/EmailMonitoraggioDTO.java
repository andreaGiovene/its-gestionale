package com.its.gestionale.dto;

import lombok.Data;

/**
 * DTO per richiesta invio email di monitoraggio.
 */
@Data
public class EmailMonitoraggioDTO {

    /** ID del tirocinio associato al monitoraggio */
    private Integer tirocinioId;

    /** Email destinatario (azienda) */
    private String emailDestinatario;
}