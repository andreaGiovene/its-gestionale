package com.its.gestionale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Corso;

@Repository 
public interface CorsoRepository extends JpaRepository<Corso, Integer> {
    
    
    List<Corso> findByStato(Corso.StatoCorso stato);

    List<Corso> findByNomeContainingIgnoreCase(String nome);
    
}
