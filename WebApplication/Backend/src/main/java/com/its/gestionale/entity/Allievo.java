package com.its.gestionale.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "allievo")
public class Allievo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cognome;

    @ManyToOne
    @JoinColumn(name = "corso_id") // deve esistere nel DB
    private Corso corso;

    public Allievo() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public Corso getCorso() { return corso; }
    public void setCorso(Corso corso) { this.corso = corso; }
}