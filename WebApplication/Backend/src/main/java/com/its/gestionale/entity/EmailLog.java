package com.its.gestionale.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entità che rappresenta uno storico delle email inviate dal sistema.
 */
@Entity
@Table(name = "email_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Destinatario email */
    private String destinatario;

    /** Oggetto email */
    private String oggetto;

    /** Contenuto email */
    @Column(columnDefinition = "TEXT")
    private String testo;

    /** Timestamp invio */
    private LocalDateTime dataInvio;
}