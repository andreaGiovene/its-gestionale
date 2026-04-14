package com.its.gestionale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Azienda;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Integer>, JpaSpecificationExecutor<Azienda> {
}
