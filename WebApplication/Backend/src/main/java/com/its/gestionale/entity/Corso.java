package com.its.gestionale.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "nome_corso", nullable = false)      // ← La colonna NON può essere NULL
    private String nome;

    @Column(name = "anno_accademico")                     // ← Specifica il nome colonna nel DB
    private String annoAccademico;                        //   (Java usa camelCase, DB usa snake_case)

    @Column(nullable = false)
    private String stato;

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