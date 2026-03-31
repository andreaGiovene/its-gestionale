package com.its.gestionale.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CorsoDTO {

    private Integer id;

    @NotBlank(message = "Il nome del corso e obbligatorio")
    @Size(max = 255, message = "Il nome del corso non puo superare 255 caratteri")
    private String nome;

    @Size(max = 32, message = "L'anno accademico non puo superare 32 caratteri")
    private String annoAccademico;

    @NotBlank(message = "Lo stato del corso e obbligatorio")
    @Size(max = 32, message = "Lo stato non puo superare 32 caratteri")
    private String stato;

    // Campo derivato per futura integrazione con gestione allievi.
    private Integer allieviCount;
}
