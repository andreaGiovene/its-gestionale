package com.its.gestionale.repository;

import com.its.gestionale.entity.DocumentoTirocinio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoTirocinioRepository extends JpaRepository<DocumentoTirocinio, Integer> {
    List<DocumentoTirocinio> findByTirocinioId(Integer tirocinioId);
}
