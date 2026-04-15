package com.its.gestionale.entity;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.its.gestionale.entity.enums.RuoloContatto;
import jakarta.persistence.*;
import lombok.*;

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
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "ruolo", columnDefinition = "ruolo_contatto_enum")
    @ColumnTransformer(write = "?::ruolo_contatto_enum")
    
    private RuoloContatto ruolo;

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