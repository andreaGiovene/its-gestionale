package com.its.gestionale.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Ruolo;
import com.its.gestionale.entity.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Trova un utente per username — utile per login
    Optional<Utente> findByUsername(String username);

    // Trova un utente per email
    Optional<Utente> findByEmail(String email);

    // Conta gli utenti di un determinato ruolo (utilità per statistiche)
    Long countByRuolo(Ruolo ruolo);
}
