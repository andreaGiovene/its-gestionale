package com.its.gestionale.dto;

import com.its.gestionale.entity.ContattoAziendale;
import com.its.gestionale.entity.enums.RuoloContattoAziendale;

/**
 * DTO per i contatti aziendali.
 *
 * Rappresenta i dati di un contatto aziendale esposti via API REST,
 * mantenendo separata la logica di persistenza dalla rappresentazione.
 */
public class ContattoDTO {

    private Integer id;
    private String nome;
    private String cognome;
    private RuoloContattoAziendale ruolo;
    private String telefono;
    private String email;

    // Costruttore di default
    public ContattoDTO() {
    }

    // Costruttore con parametri
    public ContattoDTO(Integer id, String nome, String cognome, RuoloContattoAziendale ruolo,
                       String telefono, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.ruolo = ruolo;
        this.telefono = telefono;
        this.email = email;
    }

    // Converte da entity a DTO
    public static ContattoDTO fromEntity(ContattoAziendale entity) {
        if (entity == null) {
            return null;
        }
        return new ContattoDTO(
            entity.getId(),
            entity.getNome(),
            entity.getCognome(),
            entity.getRuolo(),
            entity.getTelefono(),
            entity.getEmail()
        );
    }

}