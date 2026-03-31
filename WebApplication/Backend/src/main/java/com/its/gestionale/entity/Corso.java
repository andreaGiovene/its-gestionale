package com.its.gestionale.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                          // ← Dice a JPA "questa classe è una tabella"
@Table(name = "corso")
@Data                            // ← Lombok: genera automaticamente getter, setter, toString, equals
@NoArgsConstructor               // ← Lombok: genera costruttore senza parametri (richiesto da JPA)
@AllArgsConstructor              // ← Lombok: genera costruttore con tutti i parametri
public class Corso {

    @Id                                                    // ← Questa è la chiave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // ← Il DB genera l'ID automaticamente
                                                          //    (AUTO INCREMENT in PostgreSQL)
    private Integer id;

    @Column(nullable = false)                             // ← La colonna NON può essere NULL
    private String nome;

    @Column(name = "anno_accademico")                     // ← Specifica il nome colonna nel DB
    private String annoAccademico;                        //   (Java usa camelCase, DB usa snake_case)

    @Enumerated(EnumType.STRING)   // ← Salva l'enum come stringa nel DB (es. "ATTIVO")
                                   //   invece che come numero (0, 1, 2) — molto più leggibile
    @Column(nullable = false)
    private StatoCorso stato;

    // Enum annidato — definisce i valori ammessi per "stato"
    public enum StatoCorso {
        ATTIVO,
        CONCLUSO,
        IN_PIANIFICAZIONE
    }
@OneToMany(mappedBy = "corso")
// ↑ mappedBy = "corso" significa:
//   "la relazione è già mappata dal campo 'corso'
//    nella classe Allievo — non creare una seconda
//    colonna, usa quella già esistente"
// Senza mappedBy Hibernate creerebbe una tabella
// di join intermedia — non è quello che vogliamo!
@JsonIgnore
private List<Allievo> allievi;
}