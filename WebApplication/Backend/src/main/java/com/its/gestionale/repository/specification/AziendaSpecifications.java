package com.its.gestionale.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.enums.TipoAzienda;

import jakarta.persistence.criteria.JoinType;

/**
 * Factory di filtri dinamici (Specification) per l'entity Azienda.
 *
 * Questa classe centralizza la costruzione dei predicati JPA usati
 * dalla ricerca aziende nel service, così la logica dei filtri resta
 * riusabile, componibile e facile da estendere.
 */
public final class AziendaSpecifications {

    private AziendaSpecifications() {
    }

    /**
     * Filtra per ragione sociale con ricerca case-insensitive e match parziale.
     *
     * Se il valore è nullo o vuoto, non applica alcun filtro.
     */
    public static Specification<Azienda> ragioneSocialeContains(String ragioneSociale) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(ragioneSociale)) {
                return criteriaBuilder.conjunction();
            }

            String normalized = "%" + ragioneSociale.toLowerCase().trim() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("ragioneSociale")), normalized);
        };
    }

    /**
     * Filtra le aziende associate come madrine a uno specifico corso.
     *
     * Se corsoId è nullo, non applica alcun filtro.
     * Usa una join sulla relazione Azienda -> corsiConAziendaMadrina.
     */
    public static Specification<Azienda> hasCorsoMadrina(Integer corsoId) {
        return (root, query, criteriaBuilder) -> {
            if (corsoId == null) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);
            return criteriaBuilder.equal(root.join("corsiConAziendaMadrina", JoinType.INNER).get("id"), corsoId);
        };
    }

    /**
     * Filtra per tipo applicativo azienda usando la colonna persistita "tipo".
     *
     * Se tipoAzienda è nullo, non applica alcun filtro (filtro opzionale).
     * Questo permette di usare il metodo sia in ricerche specifiche che generiche.
     */
    public static Specification<Azienda> hasTipoAzienda(TipoAzienda tipoAzienda) {
        return (root, query, criteriaBuilder) -> {
            // Filtro opzionale: se non specificato, ritorna condizione vera per includere tutti i record
            if (tipoAzienda == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("tipo"), tipoAzienda);
        };
    }
}