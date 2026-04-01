package com.its.gestionale.entity;

import java.time.LocalDate;
import java.util.List;

import com.its.gestionale.entity.enums.StatoEsito;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tirocinio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tirocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allievo_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Allievo allievo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Azienda azienda;

    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(length = 50)
    private String tipo;

    @Enumerated(EnumType.STRING)
    private StatoEsito esito;

    @Column(length = 50)
    private String frequenza;

    @OneToMany(mappedBy = "tirocinio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DocumentoTirocinio> documenti;

    @OneToMany(mappedBy = "tirocinio", fetch = FetchType.LAZY)
    private List<Monitoraggio> monitoraggi;
}
