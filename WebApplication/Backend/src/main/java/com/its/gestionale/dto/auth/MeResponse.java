package com.its.gestionale.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponse {

    private Long idUtente;
    private String email;
    private String username;
    private String ruolo;
    private Boolean attivo;
}
