package com.its.gestionale.repository;

import com.its.gestionale.entity.Corso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorsoRepository extends JpaRepository<Corso, Integer> {
}