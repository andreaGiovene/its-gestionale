package com.its.gestionale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.its.gestionale.entity.Allievo;

@Repository
public interface AllievoRepository extends JpaRepository<Allievo, Integer> {

    // Query custom con fetch join per caricare il Corso insieme agli Allievi.
    // Evita il problema N+1 quando il service converte ogni entity in DTO.
    @Query("""
        select distinct a
        from Allievo a
        left join fetch a.corso
        """)
    List<Allievo> findAllWithCorso();

    // Query custom di ricerca testuale trasversale.
    // Usa LIKE con concat('%', :search, '%') per il match "contiene"
    // e lower(...) per rendere il confronto case-insensitive.
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