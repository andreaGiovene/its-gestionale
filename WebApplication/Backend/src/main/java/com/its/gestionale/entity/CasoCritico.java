package com.its.gestionale.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "caso_critico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasoCritico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allievo_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Allievo allievo;

    @Column(name = "data_segnalazione", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate dataSegnalazione;

    @Column(name = "tipo_criticita", length = 250)
    private String tipoCriticita;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean risolto;
}
