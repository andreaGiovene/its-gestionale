package com.its.gestionale.dto;

import com.its.gestionale.entity.Utente;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UtenteDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String ruolo; // Stringa per semplicità in JSON
    private String nome;
    private String cognome;
    private Boolean attivo;

    // Metodo statico di conversione Entity → DTO
    public static UtenteDTO fromEntity(Utente utente) {
        UtenteDTO dto = new UtenteDTO();
        dto.setId(utente.getId());
        dto.setUsername(utente.getUsername());
        // ↓ Non includi la password nel DTO—non deve tornare al client per sicurezza
        // dto.setPassword(utente.getPassword());
        dto.setEmail(utente.getEmail());
        dto.setRuolo(utente.getRuolo().toString());
        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());
        dto.setAttivo(utente.getAttivo());

        return dto;
    }

    // Metodo di conversione DTO → Entity (per insert/update)
    public static Utente toEntity(UtenteDTO dto) {
        Utente utente = new Utente();
        utente.setId(dto.getId());
        utente.setUsername(dto.getUsername());
        utente.setPassword(dto.getPassword());
        utente.setEmail(dto.getEmail());
        // Converte la stringa del ruolo in enum
        utente.setRuolo(Utente.RuoloUtente.valueOf(dto.getRuolo()));
        utente.setNome(dto.getNome());
        utente.setCognome(dto.getCognome());
        utente.setAttivo(dto.getAttivo() != null ? dto.getAttivo() : true);

        return utente;
    }
}
