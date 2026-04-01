package com.its.gestionale.entity;

import java.util.List;

import jakarta.persistence.*;
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
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruolo")
    private Integer idRuolo;

    @Column(nullable = false, unique = true, length = 50)
    private String codice;

    @Column(length = 255)
    private String descrizione;

    @OneToMany(mappedBy = "ruolo", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Utente> utenti;
}


   