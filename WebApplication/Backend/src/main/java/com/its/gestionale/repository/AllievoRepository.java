package com.its.gestionale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Allievo;

@Repository
public interface AllievoRepository extends JpaRepository<Allievo, Integer> {

    @Query("""
        select distinct a
        from Allievo a
        left join fetch a.corso
        """)
    List<Allievo> findAllWithCorso();

    @Query("""
        select distinct a
        from Allievo a
        left join fetch a.corso
        where lower(a.nome) like lower(concat('%', :search, '%'))
           or lower(a.cognome) like lower(concat('%', :search, '%'))
           or lower(a.codiceFiscale) like lower(concat('%', :search, '%'))
        """)
        List<Allievo> searchByText(@Param("search") String search);

    // Tutti gli allievi di un corso specifico
    List<Allievo> findByCorsoId(Integer corsoId);
    
    // Allievi senza tirocinio associato
    List<Allievo> findByTirociniIsEmpty();

    // Ricerca per cognome (case insensitive)
    List<Allievo> findByCognomeContainingIgnoreCase(String cognome);

    // Cerca per codice fiscale (univoco → Optional)
    java.util.Optional<Allievo> findByCodiceFiscale(String codiceFiscale);

    boolean existsByCodiceFiscale(String codiceFiscale);
}