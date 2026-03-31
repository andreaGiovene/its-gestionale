package com.its.gestionale.repository;

import com.its.gestionale.entity.Tirocinio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TirocinioRepository extends JpaRepository<Tirocinio, Integer> {
    List<Tirocinio> findByAllievoId(Integer allievoId);
    List<Tirocinio> findByAziendaId(Integer aziendaId);
    List<Tirocinio> findByDataInizioGreaterThanEqualAndDataFineBeforeOrEqual(LocalDate start, LocalDate end);
}
