package com.its.gestionale.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long id;

    @Column(nullable = false, unique = true)
    // ↑ unique = true → PostgreSQL crea un indice univoco
    //   Non possono esistere due utenti con lo stesso username
    private String username;

    @Column(nullable = false)
    // ↑ In produzione: usare bcrypt o Argon2 per hash password
    //   Qui: placeholder per il modello
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    // ↑ Salva il ruolo come stringa nel DB (es. "AMMINISTRATORE")
    //   invece che come numero — molto più leggibile
    @Column(nullable = false)
    private RuoloUtente ruolo;

    @Column(nullable = true)
    private String nome;

    @Column(nullable = true)
    private String cognome;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    // ↑ true = utente attivo, false = disabilitato
    private Boolean attivo;

    // ─────────────────────────────────────────────────────────────
    // ENUM: Definisce i ruoli disponibili nel sistema
    // ─────────────────────────────────────────────────────────────
    // AMMINISTRATORE: accesso completo al sistema
    // DIDATTICA: gestione corsi, allievi e documenti
    // JOB_PLACEMENT: gestione colloqui, stage e aziende
    // TUTOR: monitoraggio tirocini e comunicazioni
    // DIREZIONE: accesso in sola lettura a dashboard/report
    // VISUALIZZATORE: ruolo legacy mantenuto per retrocompatibilita
    public enum RuoloUtente {
        AMMINISTRATORE,    // Accesso completo
        DIDATTICA,         // Corsi, allievi, documenti
        JOB_PLACEMENT,     // Colloqui, tirocini, aziende
        TUTOR,             // Monitoraggi e tirocini
        DIREZIONE,         // Dashboard e report (read-only)
        VISUALIZZATORE     // Legacy read-only
    }
}
