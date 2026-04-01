package com.its.gestionale.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AziendaDTO {

    private Integer id;

    @NotBlank(message = "La ragione sociale è obbligatoria")
    @Size(max = 100, message = "La ragione sociale non può superare 100 caratteri")
    private String ragioneSociale;

    @NotBlank(message = "La partita IVA è obbligatoria")
    @Size(max = 20, message = "La partita IVA non può superare 20 caratteri")
    private String partitaIva;

    @Size(max = 20, message = "Il telefono non può superare 20 caratteri")
    private String telefono;

    @Size(max = 100, message = "L'email non può superare 100 caratteri")
    private String email;

    @Size(max = 150, message = "L'indirizzo non può superare 150 caratteri")
    private String indirizzo;

    @Size(max = 10, message = "Il CAP non può superare 10 caratteri")
    private String cap;
}
