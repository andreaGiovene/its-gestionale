package com.its.gestionale.repository;

import com.its.gestionale.entity.Responsabile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsabileRepository extends JpaRepository<Responsabile, Integer> {
    Optional<Responsabile> findByCodiceFiscale(String codiceFiscale);
    Optional<Responsabile> findByEmail(String email);
    Optional<Responsabile> findByUtenteId(Integer utenteId);
}
