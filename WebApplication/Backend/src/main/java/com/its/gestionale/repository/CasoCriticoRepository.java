package com.its.gestionale.repository;

import com.its.gestionale.entity.CasoCritico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasoCriticoRepository extends JpaRepository<CasoCritico, Integer> {
    List<CasoCritico> findByAllievoId(Integer allievoId);
    List<CasoCritico> findByRisolteFalse();
}
