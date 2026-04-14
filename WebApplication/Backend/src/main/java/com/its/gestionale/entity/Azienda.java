package com.its.gestionale.entity;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.its.gestionale.entity.enums.TipoAzienda;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity JPA per l'azienda (partner del percorso di tirocinio).
 *
 * Rappresenta l'azienda come entry point nella gestione dei tirocini e del rapporto
 * azienda-ITS. Ogni azienda è classificata come MADRINA (sede principale del tirocinio)
 * o NON_MADRINA (eventuale partner secondaria).
 *
 * Mapping relazionale:
 * - OneToMany: contatti aziendali, tirocini ospitati
 * - Inverse relationship: corsi per i quali l'azienda è madrina
 *
 * Campo enum 'tipo':
 * - Mappato a PostgreSQL enum nativo tipo_azienda_enum (valori: MADRINA, NON_MADRINA)
 * - NOT NULL obbligatorio a livello DB e applicazione
 * - Annotazioni: @JdbcTypeCode(SqlTypes.NAMED_ENUM), @Enumerated(EnumType.STRING), @NotNull
 *
 * Validazioni:
 * - ragioneSociale: max 100 char
 * - partitaIva: max 20 char
 * - tipo: obbligatorio (enum MADRINA|NON_MADRINA)
 * - contatti e tirocini caricati con FetchType.LAZY per ottimizzazione
 *
 * @see TipoAzienda
 * @see ContattoAziendale
 * @see Tirocinio
 * @see Corso
 */
@Entity
@Table(name = "azienda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Azienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ragione_sociale", length = 100)
    private String ragioneSociale;

    @Column(name = "partita_iva", length = 20)
    private String partitaIva;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 150)
    private String indirizzo;

    @Column(length = 10)
    private String cap;

    @Column(length = 100)
    private String citta;

    @NotNull(message = "Il tipo di azienda è obbligatorio")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition = "tipo_azienda_enum")
    private TipoAzienda tipo;

    @OneToMany(mappedBy = "azienda", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContattoAziendale> contatti;

    @OneToMany(mappedBy = "azienda", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tirocinio> tirocini;

    @OneToMany(mappedBy = "aziendaMadrina", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Corso> corsiConAziendaMadrina;
}
