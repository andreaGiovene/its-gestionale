package com.its.gestionale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.its.gestionale.entity.EmailLog;

/**
 * Repository per l'entità EmailLog, che rappresenta lo storico delle email inviate.
 */
public interface EmailLogRepository extends JpaRepository<EmailLog, Integer> {
}