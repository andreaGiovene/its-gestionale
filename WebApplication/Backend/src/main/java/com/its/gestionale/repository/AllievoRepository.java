package com.its.gestionale.repository;

import com.its.gestionale.entity.Allievo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllievoRepository extends JpaRepository<Allievo, Integer> {
}