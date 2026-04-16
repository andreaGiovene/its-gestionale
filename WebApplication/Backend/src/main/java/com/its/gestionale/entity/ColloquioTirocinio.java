package com.its.gestionale.entity;

import java.time.LocalDate;

import com.its.gestionale.entity.enums.StatoEsito;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "colloquio_tirocinio")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entity che rappresenta un colloquio tra un allievo e un'azienda nel contesto tirocinio.
 *
 * <p>La classe modella sia i riferimenti anagrafici (allievo/azienda) sia i dati
 * operativi del colloquio (data, tipologia evento, esito e feedback).
 */
public class ColloquioTirocinio {

    /** Identificativo univoco del colloquio. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Allievo coinvolto nel colloquio.
     *
     * <p>Fetch LAZY per evitare il caricamento automatico dell'intera anagrafica
     * quando servono solo i dati del colloquio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allievo_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Allievo allievo;

    /**
     * Azienda con cui e stato sostenuto il colloquio.
     *
     * <p>Anche qui il fetch LAZY riduce query e payload non necessari nei listati.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Azienda azienda;

    /** Data in cui si e svolto il colloquio. */
    @Column(name = "data_colloquio", nullable = false)
    private LocalDate dataColloquio;

    /**
     * Tipologia dell'evento (es. colloquio conoscitivo, tecnico, finale).
     * Campo opzionale, usato per dettagliare il processo di selezione.
     */
    @Column(name = "tipo_evento", length = 100)
    private String tipoEvento;

    /**
     * Stato/esito del colloquio.
     *
     * <p>Persistito come stringa per mantenere leggibilita a DB; default previsto:
     * {@code IN_ATTESA}.
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'IN_ATTESA'")
    private StatoEsito esito;

    /** Note qualitative o feedback raccolti durante/dopo il colloquio. */
    @Column(name = "note_feedback", columnDefinition = "TEXT")
    private String noteFeedback;
}
