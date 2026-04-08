package com.its.gestionale.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "monitoraggio")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entita che rappresenta una rilevazione di monitoraggio relativa a un tirocinio.
 * <p>
 * Ogni record traccia la data del monitoraggio, il responsabile che lo ha
 * effettuato e le eventuali note di dettaglio.
 */
public class Monitoraggio {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Tirocinio a cui il monitoraggio fa riferimento. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tirocinio_id")
    @ToString.Exclude
    
    
    /** Esclude il campo dal calcolo di equals/hashCode generato da Lombok.
        Evita problemi di ricorsione infinita in caso di relazioni bidirezionali. */
    @EqualsAndHashCode.Exclude
    private Tirocinio tirocinio; // relazione many-to-one con Tirocinio

    /** Data in cui e stato effettuato il monitoraggio. */
    @Column(name = "data_monitoraggio")
    private LocalDate dataMonitoraggio;

    /** Identificativo del responsabile che ha effettuato il monitoraggio. */
    @Column(name = "responsabile")
    private Integer responsabileId;

    /** Annotazioni testuali libere associate al monitoraggio. */
    @Column(columnDefinition = "TEXT")
    private String note;
}
