package com.its.gestionale.entity;

import com.its.gestionale.entity.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "documento_tirocinio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoTirocinio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tirocinio_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Tirocinio tirocinio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean presente;

    @Column(name = "data_acquisizione")
    private LocalDate dataAcquisizione;
}
