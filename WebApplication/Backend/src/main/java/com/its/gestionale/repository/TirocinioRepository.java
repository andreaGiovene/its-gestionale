package com.its.gestionale.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Tirocinio;

@Repository
public interface TirocinioRepository extends JpaRepository<Tirocinio, Integer> {
    List<Tirocinio> findByAllievoId(Integer allievoId);
    List<Tirocinio> findByAziendaId(Integer aziendaId);
    List<Tirocinio> findByDataInizioGreaterThanEqualAndDataFineLessThanEqual(LocalDate start, LocalDate end);
}
