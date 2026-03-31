package com.its.gestionale.entity;

import com.its.gestionale.entity.enums.TipoResponsabile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "responsabile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Responsabile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(length = 50, nullable = false)
    private String cognome;

    @Column(name = "codice_fiscale", length = 16, unique = true)
    private String codiceFiscale;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    private TipoResponsabile tipo;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean attivo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", unique = true)
    private Utente utente;
}
