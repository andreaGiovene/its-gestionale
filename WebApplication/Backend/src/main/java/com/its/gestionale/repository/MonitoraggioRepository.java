package com.its.gestionale.repository;

import com.its.gestionale.entity.Monitoraggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoraggioRepository extends JpaRepository<Monitoraggio, Integer> {
    List<Monitoraggio> findByTirocinioId(Integer tirocinioId);
}
