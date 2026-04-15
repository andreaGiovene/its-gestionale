package com.its.gestionale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.ContattoAziendale;

@Repository
public interface ContattoAziendaleRepository extends JpaRepository<ContattoAziendale, Integer> {
    List<ContattoAziendale> findByAziendaId(Integer aziendaId);
    List<ContattoAziendale> findByUtenteIdUtente(Integer utenteId);
}
