package com.its.gestionale.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "azienda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Azienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ragione_sociale", length = 100)
    private String ragioneSociale;

    @Column(name = "partita_iva", length = 20)
    private String partitaIva;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 150)
    private String indirizzo;

    @Column(length = 10)
    private String cap;

    @OneToMany(mappedBy = "azienda", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContattoAziendale> contatti;

    @OneToMany(mappedBy = "azienda", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tirocinio> tirocini;
}
