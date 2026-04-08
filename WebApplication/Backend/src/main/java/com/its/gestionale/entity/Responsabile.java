package com.its.gestionale.entity;

import com.its.gestionale.entity.enums.TipoResponsabile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "responsabile")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entita che rappresenta un responsabile (es. scolastico o aziendale)
 * associabile al dominio applicativo.
 * <p>
 * Contiene dati anagrafici, contatti, stato attivo e collegamento opzionale
 * all'utenza applicativa.
 */
public class Responsabile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nome;

  
    @Column(length = 50, nullable = false)
    private String cognome;

  
    @Column(name = "codice_fiscale", length = 16, unique = true)
    private String codiceFiscale;

 
    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    /** Tipologia del responsabile secondo l'enum {@link TipoResponsabile}. */
    @Enumerated(EnumType.STRING)
    private TipoResponsabile tipo;

    /** Stato logico di attivazione del responsabile (default true). */
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean attivo;

    /** Utenza applicativa associata in modo univoco al responsabile. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", unique = true)
    private Utente utente;
}
