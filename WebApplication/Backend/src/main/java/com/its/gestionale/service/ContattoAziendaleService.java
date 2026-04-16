package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.ContattoDTO;
import com.its.gestionale.dto.auth.ContattoAziendaleDTO;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.ContattoAziendale;
import com.its.gestionale.entity.Utente;
import com.its.gestionale.exception.AziendaNotFoundException;
import com.its.gestionale.repository.AziendaRepository;
import com.its.gestionale.repository.ContattoAziendaleRepository;
import com.its.gestionale.repository.UtenteRepository;

@Service
/**
 * Service applicativo per la gestione dei contatti aziendali.
 *
 * <p>La complessita principale di questa classe e mantenere compatibili due
 * flussi DTO diversi:
 * <ul>
 *   <li>{@link ContattoAziendaleDTO}: flusso storico usato dalle API legacy;</li>
 *   <li>{@link ContattoDTO}: flusso semplificato usato dalle API piu recenti.</li>
 * </ul>
 *
 * <p>Entrambi i flussi mappano sulla stessa entity ({@link ContattoAziendale})
 * ma hanno regole di validazione e gestione errori leggermente diverse.
 */
public class ContattoAziendaleService {

    private final ContattoAziendaleRepository contattoRepository;
    private final AziendaRepository aziendaRepository;
    private final UtenteRepository utenteRepository;

    public ContattoAziendaleService(
            ContattoAziendaleRepository contattoRepository,
            AziendaRepository aziendaRepository,
            UtenteRepository utenteRepository) {
        this.contattoRepository = contattoRepository;
        this.aziendaRepository = aziendaRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Crea un contatto usando il contratto DTO legacy.
     *
     * <p>Punti delicati:
     * <ul>
     *   <li>{@code aziendaId} e obbligatorio e genera 400 se assente;</li>
     *   <li>se {@code utenteId} e presente viene verificata l'esistenza dell'utente;</li>
     *   <li>tutti gli errori di validazione applicativa vengono esposti come 400.</li>
     * </ul>
     */
    @Transactional
    public ContattoAziendaleDTO create(ContattoAziendaleDTO dto) {
        ContattoAziendale contatto = new ContattoAziendale();

        contatto.setNome(dto.getNome());
        contatto.setCognome(dto.getCognome());
        contatto.setTelefono(dto.getTelefono());
        contatto.setEmail(dto.getEmail());
        contatto.setRuolo(dto.getRuolo());

        if (dto.getAziendaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "aziendaId obbligatorio");
        }

        Azienda azienda = aziendaRepository.findById(dto.getAziendaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Azienda non trovata"));
        contatto.setAzienda(azienda);

        if (dto.getUtenteId() != null) {
            Utente utente = utenteRepository.findById(dto.getUtenteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente non trovato"));
            contatto.setUtente(utente);
        }

        return ContattoAziendaleDTO.fromEntity(contattoRepository.save(contatto));
    }

    /**
     * Restituisce i contatti di un'azienda nel formato DTO legacy.
     *
     * <p>La trasformazione e esplicita per mantenere il mapping centralizzato su
     * {@code fromEntity} ed evitare dipendenze da framework di mapping automatico.
     */
    @Transactional(readOnly = true)
    public List<ContattoAziendaleDTO> findByAziendaId(Integer aziendaId) {
        List<ContattoAziendale> contatti = contattoRepository.findByAziendaId(aziendaId);
        List<ContattoAziendaleDTO> result = new ArrayList<>();

        for (ContattoAziendale contatto : contatti) {
            result.add(ContattoAziendaleDTO.fromEntity(contatto));
        }

        return result;
    }

    /**
     * Restituisce i contatti di un'azienda nel formato DTO usato dal frontend recente.
     *
     * <p>Il metodo coesiste con {@link #findByAziendaId(Integer)} per supportare
     * controller diversi durante la fase di transizione tra i due contratti API.
     */
    @Transactional(readOnly = true)
    public List<ContattoDTO> findByAziendaIdAsDto(Integer aziendaId) {
        List<ContattoAziendale> contatti = contattoRepository.findByAziendaId(aziendaId);
        List<ContattoDTO> result = new ArrayList<>();

        for (ContattoAziendale contatto : contatti) {
            result.add(ContattoDTO.fromEntity(contatto));
        }

        return result;
    }

    /**
     * Crea un contatto agganciandolo a una specifica azienda gia nota dall'endpoint.
     *
     * <p>Qui la semantica errori e diversa dal flusso legacy: azienda assente
     * genera {@link AziendaNotFoundException}, gestita dal layer globale.
     */
    @Transactional
    public ContattoDTO createForAzienda(Integer aziendaId, ContattoDTO dto) {
        Azienda azienda = aziendaRepository.findById(aziendaId)
                .orElseThrow(() -> new AziendaNotFoundException(aziendaId));

        ContattoAziendale contatto = new ContattoAziendale();
        contatto.setAzienda(azienda);
        contatto.setNome(dto.getNome());
        contatto.setCognome(dto.getCognome());
        contatto.setRuolo(dto.getRuolo());
        contatto.setTelefono(dto.getTelefono());
        contatto.setEmail(dto.getEmail());

        return ContattoDTO.fromEntity(contattoRepository.save(contatto));
    }

    /**
     * Elimina un contatto validando prima l'esistenza per restituire 404 esplicito.
     */
    @Transactional
    public void deleteById(Integer id) {
        if (!contattoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contatto non trovato");
        }

        contattoRepository.deleteById(id);
    }
}
