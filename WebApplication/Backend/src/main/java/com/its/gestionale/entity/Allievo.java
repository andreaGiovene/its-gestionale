package com.its.gestionale.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "allievo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allievo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(name = "codice_fiscale", unique = true)
    // ↑ unique = true → PostgreSQL crea un indice univoco
    //   Non possono esistere due allievi con lo stesso CF
    private String codiceFiscale;

    
    @Column(name = "data_di_nascita")
    private LocalDate dataDiNascita;

    @Column(name= "telefono")
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String note;

    // ─────────────────────────────────────────
    // RELAZIONE: Allievo → Corso (ManyToOne)
    // ─────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corso_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Corso corso;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", unique = true)
    private Utente utente;

    @OneToMany(mappedBy = "allievo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tirocinio> tirocini;

    @OneToMany(mappedBy = "allievo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CasoCritico> casiCritici;

    @OneToMany(mappedBy = "allievo", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ColloquioTirocinio> colloqui;
}