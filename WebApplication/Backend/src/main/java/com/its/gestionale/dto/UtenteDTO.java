package com.its.gestionale.dto;

import com.its.gestionale.entity.Utente;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UtenteDTO {

    private Integer idUtente;
    private String username;
    private String password;
    private String email;
    private String ruolo;
    private Boolean attivo;

    // Metodo statico di conversione Entity → DTO
    public static UtenteDTO fromEntity(Utente utente) {
        UtenteDTO dto = new UtenteDTO();
        dto.setIdUtente(utente.getIdUtente());
        dto.setUsername(utente.getUsername());
        dto.setEmail(utente.getEmail());
        dto.setRuolo(utente.getRuolo() != null ? utente.getRuolo().getCodice() : null);
        dto.setAttivo(utente.getAttivo());

        return dto;
    }
}
