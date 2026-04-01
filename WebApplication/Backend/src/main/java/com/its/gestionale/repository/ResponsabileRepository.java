package com.its.gestionale.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Responsabile;

@Repository
public interface ResponsabileRepository extends JpaRepository<Responsabile, Integer> {
    Optional<Responsabile> findByCodiceFiscale(String codiceFiscale);
    Optional<Responsabile> findByEmail(String email);
    Optional<Responsabile> findByUtenteIdUtente(Integer utenteId);
}
