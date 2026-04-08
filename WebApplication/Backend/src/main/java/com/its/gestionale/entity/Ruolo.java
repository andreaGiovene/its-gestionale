package com.its.gestionale.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ruolo")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entità che rappresenta un ruolo applicativo assegnabile agli utenti.
 * <p>
 * Ogni ruolo e identificato da un codice univoco e può essere associato
 * a più utenti tramite la relazione inversa definita in {@link Utente}.
 */
public class Ruolo {

    /** Identificativo tecnico del ruolo (chiave primaria). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruolo")
    private Integer idRuolo;

    /** Codice funzionale univoco del ruolo (es. ADMIN, TUTOR, STUDENTE). */
    @Column(nullable = false, unique = true, length = 50)
    private String codice;

    @Column(length = 255)
    private String descrizione;

    /**
     * Utenti associati a questo ruolo.
     * <p>
     * Relazione one-to-many caricata lazy per evitare fetch non necessari.
     */
    @OneToMany(mappedBy = "ruolo", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Utente> utenti;
}


   