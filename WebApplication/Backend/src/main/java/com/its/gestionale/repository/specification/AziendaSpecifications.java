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
     * Filtra per ruolo madrina/non madrina in base alla relazione con i corsi.
     *
     * Regola di dominio:
     * - MADRINA: l'azienda è associata ad almeno un corso come azienda madrina.
     * - NON_MADRINA: l'azienda non è associata a nessun corso come azienda madrina.
     *
     * Se tipoAzienda è nullo, non applica alcun filtro.
     */
    public static Specification<Azienda> hasTipoAzienda(TipoAzienda tipoAzienda) {
        return (root, query, criteriaBuilder) -> {
            if (tipoAzienda == null) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);
            if (tipoAzienda == TipoAzienda.MADRINA) {
                return criteriaBuilder.isNotEmpty(root.get("corsiConAziendaMadrina"));
            }

            return criteriaBuilder.isEmpty(root.get("corsiConAziendaMadrina"));
        };
    }
}