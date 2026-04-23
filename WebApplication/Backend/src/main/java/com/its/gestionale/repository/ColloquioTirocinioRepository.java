package com.its.gestionale.repository;

import com.its.gestionale.entity.ColloquioTirocinio;
import com.its.gestionale.entity.enums.StatoEsitoColloquio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ColloquioTirocinioRepository extends JpaRepository<ColloquioTirocinio, Integer> {
    List<ColloquioTirocinio> findByAllievoId(Integer allievoId);
    List<ColloquioTirocinio> findByAziendaId(Integer aziendaId);
    List<ColloquioTirocinio> findByAllievoIdAndAziendaId(Integer allievoId, Integer aziendaId);
    List<ColloquioTirocinio> findByDataColloquioBetween(LocalDate start, LocalDate end);
    List<ColloquioTirocinio> findByEsito(StatoEsitoColloquio esito);
    long countByAllievoIdAndEsito(Integer allievoId, StatoEsitoColloquio esito);
    long countByAziendaIdAndDataColloquioBetween(Integer aziendaId, LocalDate start, LocalDate end);
}
