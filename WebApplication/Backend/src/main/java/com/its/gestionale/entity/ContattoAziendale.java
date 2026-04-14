package com.its.gestionale.entity;

import com.its.gestionale.entity.enums.RuoloContattoAziendale;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contatto_aziendale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContattoAziendale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id")
    private Azienda azienda;

    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo")
    private RuoloContattoAziendale ruolo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente")
    private Utente utente;
}
