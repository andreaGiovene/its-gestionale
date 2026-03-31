package com.its.gestionale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.CasoCritico;

@Repository
public interface CasoCriticoRepository extends JpaRepository<CasoCritico, Integer> {
    List<CasoCritico> findByAllievoId(Integer allievoId);
    List<CasoCritico> findByRisoltoFalse();
}
