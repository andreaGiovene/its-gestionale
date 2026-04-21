package com.its.gestionale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Allievo;

@Repository
public interface AllievoRepository extends JpaRepository<Allievo, Integer> {

    // Tutti gli allievi di un corso specifico
    List<Allievo> findByCorsoId(Integer corsoId);

    // Ricerca per cognome (case insensitive)
    List<Allievo> findByCognomeContainingIgnoreCase(String cognome);

    // Cerca per codice fiscale (univoco → Optional)
    java.util.Optional<Allievo> findByCodiceFiscale(String codiceFiscale);

    boolean existsByCodiceFiscale(String codiceFiscale);
}