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
public class ColloquioTirocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allievo_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Allievo allievo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Azienda azienda;

    @Column(name = "data_colloquio", nullable = false)
    private LocalDate dataColloquio;

    @Column(name = "tipo_evento", length = 100)
    private String tipoEvento;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'IN_ATTESA'")
    private StatoEsito esito;

    @Column(name = "note_feedback", columnDefinition = "TEXT")
    private String noteFeedback;
}
