package com.its.gestionale.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponse {

    private Integer idUtente;
    private String email;
    private String ruolo;
    private Boolean attivo;
}
