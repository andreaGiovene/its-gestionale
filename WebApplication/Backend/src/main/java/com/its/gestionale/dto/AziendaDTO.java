package com.its.gestionale.dto;

import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.enums.TipoAzienda;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object per l'entity Azienda.
 *
 * Rappresenta i dati di un'azienda esposti dall'API REST, con validazioni
 * dichiarative per garantire integrità dei dati in ingresso (annotazioni @NotBlank, @NotNull, @Size).
 * Disaccoppia il modello di persistenza (JPA) dalla rappresentazione HTTP, consentendo
 * evoluzione indipendente dell'API e del database.
 *
 * Il metodo statico {@code fromEntity()} consente la conversione bidirezionale entity ↔ DTO.
 *
 * Validazioni applicate:
 * - ragioneSociale: obbligatoria, 1-100 caratteri
 * - partitaIva: obbligatoria, 1-20 caratteri
 * - tipoAzienda: obbligatorio, enum MADRINA|NON_MADRINA
 * - campi facoltativi (telefono, email, etc.) con vincoli di lunghezza
 *
 * @see Azienda
 * @see TipoAzienda
 */
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

    @Size(max = 100, message = "La città non può superare 100 caratteri")
    private String citta;

    @NotNull(message = "Il tipo di azienda è obbligatorio")
    private TipoAzienda tipoAzienda;

    /**
     * Converte un'entity JPA in DTO per esposizione REST.
     *
     * Questa conversione centralizzata garantisce allineamento fra entity e DTO
     * e facilita futuri interventi se i campi divergessero. Il mapping è manuale
     * (non MapStruct) per chiarezza e tracciabilità dei campi esposti.
     *
     * @param azienda entity da convertire (non null)
     * @return DTO con tutti i campi valorizzati dall'entity
     */
    public static AziendaDTO fromEntity(Azienda azienda) {
        AziendaDTO dto = new AziendaDTO();
        dto.setId(azienda.getId());
        dto.setRagioneSociale(azienda.getRagioneSociale());
        dto.setPartitaIva(azienda.getPartitaIva());
        dto.setTelefono(azienda.getTelefono());
        dto.setEmail(azienda.getEmail());
        dto.setIndirizzo(azienda.getIndirizzo());
        dto.setCap(azienda.getCap());
        dto.setCitta(azienda.getCitta());
        dto.setTipoAzienda(azienda.getTipo());
        return dto;
    }
}

