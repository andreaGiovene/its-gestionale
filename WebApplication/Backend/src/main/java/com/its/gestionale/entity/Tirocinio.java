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
/**
 * Entita che rappresenta un tirocinio nel percorso formativo.
 * <p>
 * Collega un allievo a una azienda e contiene le principali informazioni
 * gestionali (periodo, tipologia, esito, frequenza), oltre alle entita
 * correlate di documentazione e monitoraggio.
 */
public class Tirocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Allievo a cui e associato il tirocinio. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allievo_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Allievo allievo;

    /** Azienda ospitante presso cui si svolge il tirocinio. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Azienda azienda;

  
    @Column(name = "data_inizio")
    private LocalDate dataInizio;


    @Column(name = "data_fine")
    private LocalDate dataFine;

    /** Tipologia del tirocinio (es. curriculare/extracurriculare). */
    @Column(length = 50)
    private String tipo;

    /** Stato/esito del tirocinio secondo l'enum {@link StatoEsito}. */
    @Enumerated(EnumType.STRING)
    private StatoEsito esito;

    /** Frequenza prevista o registrata (es. full-time, part-time). */
    @Column(length = 50)
    private String frequenza;

    /**
     * Documenti collegati al tirocinio.
     * <p>
     * Cascade ALL per propagare operazioni di persistenza/rimozione ai documenti.
     */
    @OneToMany(mappedBy = "tirocinio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DocumentoTirocinio> documenti;

    /** Monitoraggi periodici associati al tirocinio. */
    @OneToMany(mappedBy = "tirocinio", fetch = FetchType.LAZY)
    private List<Monitoraggio> monitoraggi;
}
