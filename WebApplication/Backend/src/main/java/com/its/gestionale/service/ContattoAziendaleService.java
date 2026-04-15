package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.its.gestionale.dto.auth.ContattoAziendaleDTO;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.ContattoAziendale;
import com.its.gestionale.entity.Utente;
import com.its.gestionale.repository.AziendaRepository;
import com.its.gestionale.repository.ContattoAziendaleRepository;
import com.its.gestionale.repository.UtenteRepository;

@Service
public class ContattoAziendaleService {

    private final ContattoAziendaleRepository contattoRepository;
    private final AziendaRepository aziendaRepository;
    private final UtenteRepository utenteRepository;

    public ContattoAziendaleService(ContattoAziendaleRepository contattoRepository,
                                AziendaRepository aziendaRepository,
                                UtenteRepository utenteRepository) {
        this.contattoRepository = contattoRepository;
        this.aziendaRepository = aziendaRepository;
        this.utenteRepository = utenteRepository;
    }

    @Transactional
    public ContattoAziendaleDTO create(ContattoAziendaleDTO dto) {

        ContattoAziendale contatto = new ContattoAziendale();

        contatto.setNome(dto.getNome());
        contatto.setCognome(dto.getCognome());
        contatto.setTelefono(dto.getTelefono());
        contatto.setEmail(dto.getEmail());
        contatto.setRuolo(dto.getRuolo());

        // Collegamento Azienda (OBBLIGATORIO)
        if (dto.getAziendaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "aziendaId obbligatorio");
        }

        Azienda azienda = aziendaRepository.findById(dto.getAziendaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Azienda non trovata"
                ));

        contatto.setAzienda(azienda);

        // Collegamento Utente (OPZIONALE)
        if (dto.getUtenteId() != null) {
            Utente utente = utenteRepository.findById(dto.getUtenteId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Utente non trovato"
                    ));
            contatto.setUtente(utente);
        }

        return ContattoAziendaleDTO.fromEntity(contattoRepository.save(contatto));
    }

    // GET per azienda

    @Transactional(readOnly = true)
    public List<ContattoAziendaleDTO> findByAziendaId(Integer aziendaId) {

        List<ContattoAziendale> contatti = contattoRepository.findByAziendaId(aziendaId);
        List<ContattoAziendaleDTO> result = new ArrayList<>();

        for (ContattoAziendale c : contatti) {
            result.add(ContattoAziendaleDTO.fromEntity(c));
        }

        return result;
    }

    // DELETE

    @Transactional
    public void deleteById(Integer id) {
        if (!contattoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Contatto non trovato"
            );
        }

        contattoRepository.deleteById(id);
    }
}