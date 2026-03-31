package com.its.gestionale.repository;

import com.its.gestionale.entity.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Integer> {
    List<Azienda> findByRagioneSocialeContainingIgnoreCase(String ragioneSociale);
    List<Azienda> findByPartitaIva(String partitaIva);
}
