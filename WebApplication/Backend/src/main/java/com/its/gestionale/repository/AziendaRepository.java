package com.its.gestionale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Azienda;

import java.util.Optional;

/**
 * Repository JPA per l'entity Azienda.
 *
 * Estende:
 * - JpaRepository → fornisce CRUD base (save, findById, delete, ecc.)
 * - JpaSpecificationExecutor → permette query dinamiche con Specification (filtri avanzati)
 *
 * Contiene metodi custom utilizzati per:
 * - deduplicazione dati (es. import Excel)
 * - ricerca per attributi specifici
 */
@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Integer>,
        JpaSpecificationExecutor<Azienda> {

    /**
     * Verifica se esiste già un'azienda con la stessa partita IVA.
     *
     * Utilizzato principalmente per:
     * - evitare duplicati durante inserimento manuale
     * - deduplicazione durante import da Excel (BF-018)
     *
     * @param partitaIva partita IVA da verificare
     * @return true se esiste già un record con quella partita IVA
     */
    boolean existsByRagioneSociale(String ragioneSociale);

    /**
     * Recupera un'azienda tramite partita IVA.
     *
     * Utile per:
     * - aggiornare un record esistente invece di crearne uno nuovo
     * - controlli logici nel service
     *
     * @param partitaIva partita IVA da cercare
     * @return Optional contenente l'azienda se trovata
     */
    Optional<Azienda> findByPartitaIva(String partitaIva);
}