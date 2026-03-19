package com.its.gestionale.entity;
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
@Table(name = "allievo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allievo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(name = "codice_fiscale", unique = true)
    // ↑ unique = true → PostgreSQL crea un indice univoco
    //   Non possono esistere due allievi con lo stesso CF
    private String codiceFiscale;

    private String email;

    private String telefono;

    // ─────────────────────────────────────────
    // RELAZIONE: Allievo → Corso (ManyToOne)
    // ─────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    // ↑ fetch = LAZY significa: "non caricare il Corso
    //   automaticamente quando carichi un Allievo"
    //   Lo carichi solo se lo richiedi esplicitamente.
    //   L'alternativa è EAGER (carica sempre tutto subito)
    //   LAZY è più efficiente — evita query inutili

    @JoinColumn(name = "corso_id")
    // ↑ Dice ad Hibernate: "la chiave esterna nella tabella
    //   'allievi' si chiama 'corso_id'"
    private Corso corso;
    // ↑ Non è un Long id, ma l'oggetto Corso intero!
    //   Hibernate gestisce la traduzione automaticamente
}