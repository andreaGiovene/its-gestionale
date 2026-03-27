package com.its.gestionale.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "corso")
public class Corso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_corso")
    private String nome;

    @Column(name = "anno_accademico")
    private String annoAccademico;

    @Column(name = "stato")
    private String stato;

    // --- COSTRUTTORI ---

    public Corso() {}

    public Corso(String nome, String annoAccademico, String stato) {
        this.nome = nome;
        this.annoAccademico = annoAccademico;
        this.stato = stato;
    }

    // --- GETTER & SETTER ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAnnoAccademico() {
        return annoAccademico;
    }

    public void setAnnoAccademico(String annoAccademico) {
        this.annoAccademico = annoAccademico;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}