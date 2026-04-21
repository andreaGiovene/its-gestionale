package com.its.gestionale.entity;

import com.its.gestionale.entity.enums.RuoloContattoAziendale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity che rappresenta un contatto aziendale.
 * Un contatto è una persona di riferimento all'interno di un'azienda
 * (es. HR, tutor aziendale, referente).
 */

@Entity
@Table(name = "contatto_aziendale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContattoAziendale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo", columnDefinition = "ruolo_contatto_enum")
    private RuoloContattoAziendale ruolo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    // RELAZIONE → AZIENDA (ManysToOne)
    // UN AZIENDA PUÒ AVERE PIÙ CONTATTI
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Azienda azienda;

    // RELAZIONE → UTENTE (OneToOne)
    //COLLEGAMENTO OPZIONALE CON UN UTENTE DEL SISTEMA (ES. REFERENTE CHE HA ACCESSO ALLA PIATTAFORMA)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utente utente;
}