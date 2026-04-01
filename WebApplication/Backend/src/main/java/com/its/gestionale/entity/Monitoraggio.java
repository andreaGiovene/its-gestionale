package com.its.gestionale.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "monitoraggio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monitoraggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tirocinio_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Tirocinio tirocinio;

    @Column(name = "data_monitoraggio")
    private LocalDate dataMonitoraggio;

    @Column(name = "responsabile")
    private Integer responsabileId;

    @Column(columnDefinition = "TEXT")
    private String note;
}
