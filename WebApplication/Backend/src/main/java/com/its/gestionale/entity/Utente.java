package com.its.gestionale.entity;

import java.time.LocalDateTime;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente")
    private Integer idUtente;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean attivo;

    @Column(name = "creato_il", nullable = false)
    private LocalDateTime creatoIl;

    @Column(name = "aggiornato_il", nullable = false)
    private LocalDateTime aggiornatoIl;

    @Column(name = "ultimo_accesso")
    private LocalDateTime ultimoAccesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruolo", nullable = false)
    private Ruolo ruolo;

}
