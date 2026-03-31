package com.its.gestionale.repository;

import com.its.gestionale.entity.ContattoAziendale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContattoAzienddaleRepository extends JpaRepository<ContattoAziendale, Integer> {
    List<ContattoAziendale> findByAziendaId(Integer aziendaId);
    List<ContattoAziendale> findByUtenteId(Integer utenteId);
}
