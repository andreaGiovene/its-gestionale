package com.its.gestionale.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruolo")
    private Integer idRuolo;

    @Column(nullable = false, unique = true, length = 50)
    private String codice;

    @Column(length = 255)
    private String descrizione;

    @OneToMany(mappedBy = "ruolo")
    @ToString.Exclude         // ← evita loop toString → ruolo → utenti → ruolo...
    @EqualsAndHashCode.Exclude
    private List<Utente> utenti;

}


   