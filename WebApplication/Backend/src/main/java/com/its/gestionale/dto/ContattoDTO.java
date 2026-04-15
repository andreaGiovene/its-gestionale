package com.its.gestionale.dto;

import com.its.gestionale.entity.ContattoAziendale;
import com.its.gestionale.entity.enums.RuoloContattoAziendale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per i contatti aziendali.
 *
 * Rappresenta i dati di un contatto aziendale esposti via API REST,
 * mantenendo separata la logica di persistenza dalla rappresentazione.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContattoDTO {

    private Integer id;

    @NotBlank(message = "Il nome del contatto è obbligatorio")
    @Size(max = 50, message = "Il nome non può superare 50 caratteri")
    private String nome;

    @Size(max = 50, message = "Il cognome non può superare 50 caratteri")
    private String cognome;

    @NotNull(message = "Il ruolo del contatto è obbligatorio")
    private RuoloContattoAziendale ruolo;

    @Size(max = 20, message = "Il telefono non può superare 20 caratteri")
    private String telefono;

    @Size(max = 100, message = "L'email non può superare 100 caratteri")
    private String email;

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