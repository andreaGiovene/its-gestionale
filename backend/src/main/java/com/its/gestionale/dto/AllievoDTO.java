package com.its.gestionale.dto;

import com.its.gestionale.entity.Allievo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllievoDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String telefono;
    private java.time.LocalDate dataDiNascita;
    private String note;

    // Invece dell'oggetto Corso intero, solo le info utili
    private Long corsoId;
    private String corsoNome;

    // Metodo statico di conversione Entity → DTO
    // Centralizza la logica di mapping in un posto solo
    public static AllievoDTO fromEntity(Allievo allievo) {
        AllievoDTO dto = new AllievoDTO();
        dto.setId(allievo.getId());
        dto.setNome(allievo.getNome());
        dto.setCognome(allievo.getCognome());
        dto.setCodiceFiscale(allievo.getCodiceFiscale());
        dto.setEmail(allievo.getEmail());
        dto.setTelefono(allievo.getTelefono());
        dto.setDataDiNascita(allievo.getDataDiNascita());
        dto.setNote(allievo.getNote());

        // Estrae solo id e nome dal Corso collegato
        // (se esiste — un allievo potrebbe non avere corso)
        if (allievo.getCorso() != null) {
            dto.setCorsoId(allievo.getCorso().getId());
            dto.setCorsoNome(allievo.getCorso().getNome());
        }

        return dto;
    }
}