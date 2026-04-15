package com.its.gestionale.dto.auth;

import com.its.gestionale.entity.ContattoAziendale;
import com.its.gestionale.entity.enums.RuoloContattoAziendale;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per ContattoAziendale.
 *
 * Serve per:
 * - ricevere dati dall'API (input)
 * - restituire dati al client (output)
 *
 * Evita di esporre direttamente le Entity JPA.
 */
@Data
@NoArgsConstructor
public class ContattoAziendaleDTO {

    private Integer id;

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 50, message = "Il nome può avere massimo 50 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 50, message = "Il cognome può avere massimo 50 caratteri")
    private String cognome;

    @Size(max = 20, message = "Il telefono può avere massimo 20 caratteri")
    private String telefono;

    @Email(message = "Email non valida")
    @Size(max = 100, message = "L'email può avere massimo 100 caratteri")
    private String email;

    @NotNull(message = "Il ruolo è obbligatorio")
    private RuoloContattoAziendale ruolo;

    @NotNull(message = "aziendaId obbligatorio")
    private Integer aziendaId;

    private Integer utenteId;


    // METODO DI CONVERSIONE DA ENTITY A DTO
    //USATA PER RESTITUIRE I DATI AL CLIENT DOPO AVERLI RECUPERATI DAL DATABASE
    public static ContattoAziendaleDTO fromEntity(ContattoAziendale entity) {
        ContattoAziendaleDTO dto = new ContattoAziendaleDTO();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCognome(entity.getCognome());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setRuolo(entity.getRuolo());

        if (entity.getAzienda() != null) {
            dto.setAziendaId(entity.getAzienda().getId());
        }

        if (entity.getUtente() != null) {
            dto.setUtenteId(entity.getUtente().getIdUtente());
        }

        return dto;
    }
}