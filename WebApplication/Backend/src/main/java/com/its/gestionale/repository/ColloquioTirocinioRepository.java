package com.its.gestionale.repository;

import com.its.gestionale.entity.ColloquioTirocinio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ColloquioTirocinioRepository extends JpaRepository<ColloquioTirocinio, Integer> {
    List<ColloquioTirocinio> findByAllievoId(Integer allievoId);
    List<ColloquioTirocinio> findByAziendaId(Integer aziendaId);
    List<ColloquioTirocinio> findByDataColloquioBetween(LocalDate start, LocalDate end);
}
