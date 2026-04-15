package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.gestionale.dto.AllievoDTO;
import com.its.gestionale.dto.CorsoDTO;
import com.its.gestionale.entity.Azienda;
import com.its.gestionale.entity.Corso;
import com.its.gestionale.exception.CorsoNotFoundException;
import com.its.gestionale.repository.CorsoRepository;


@Service
public class CorsoService {

    // DEPENDENCY INJECTION — Spring crea il Repository e ce lo "inietta"
    // Non dobbiamo fare "new CorsoRepository()" manualmente
    private final CorsoRepository corsoRepository;
    private final AllievoService allievoService;

    // Costruttore — Spring vede un solo costruttore e capisce
    // automaticamente che deve iniettare il Repository
    public CorsoService(CorsoRepository corsoRepository, AllievoService allievoService) {
        this.corsoRepository = corsoRepository;
        this.allievoService = allievoService;
    }

    // Restituisce tutti i corsi
    @Transactional(readOnly = true)
    public List<CorsoDTO> findAll() {
        List<Corso> corsi = corsoRepository.findAll();
        List<CorsoDTO> result = new ArrayList<>();

        for (Corso corso : corsi) {
            CorsoDTO dto = toDto(corso);
            result.add(dto);
        }

        return result;
    }

    // Cerca un corso per ID e restituisce 404 se non esiste.
    @Transactional(readOnly = true)
    public CorsoDTO findById(Integer id) {
        Corso corso = corsoRepository.findById(id)
                .orElseThrow(() -> new CorsoNotFoundException(id));
        return toDto(corso);
    }

    // Restituisce gli allievi iscritti a un corso specifico.
    @Transactional(readOnly = true)
    public List<AllievoDTO> findAllieviByCorsoId(Integer id) {
        if (!corsoRepository.existsById(id)) {
            throw new CorsoNotFoundException(id);
        }
        return allievoService.findByCorsoId(id);
    }

    // Crea un nuovo corso.
    public CorsoDTO create(CorsoDTO dto) {
        Corso corso = toEntity(dto);
        Corso salvato = corsoRepository.save(corso);
        return toDto(salvato);
    }

    // Aggiorna un corso esistente.
    public CorsoDTO update(Integer id, CorsoDTO dto) {
        Corso esistente = corsoRepository.findById(id)
                .orElseThrow(() -> new CorsoNotFoundException(id));

        esistente.setNome(dto.getNome());
        esistente.setAnnoAccademico(dto.getAnnoAccademico());
        esistente.setStato(dto.getStato());

        Corso aggiornato = corsoRepository.save(esistente);
        return toDto(aggiornato);
    }

    // Elimina un corso per ID
    public void deleteById(Integer id) {
        if (!corsoRepository.existsById(id)) {
            throw new CorsoNotFoundException(id);
        }
        corsoRepository.deleteById(id);
    }
    
   // Cerca corsi per stato
    @Transactional(readOnly = true)
    public List<CorsoDTO> findByStato(String stato) {
        List<Corso> corsi = corsoRepository.findByStato(stato);
        List<CorsoDTO> result = new ArrayList<>();

        for (Corso corso : corsi) {
            CorsoDTO dto = toDto(corso);
            result.add(dto);
        }

        return result;
    }

    private CorsoDTO toDto(Corso corso) {
        CorsoDTO dto = new CorsoDTO();
        dto.setId(corso.getId());
        dto.setNome(corso.getNome());
        dto.setAnnoAccademico(corso.getAnnoAccademico());
        dto.setStato(corso.getStato());
        Azienda aziendaMadrina = corso.getAziendaMadrina();
        if (aziendaMadrina != null) {
            dto.setAziendaMadrinaId(aziendaMadrina.getId());
            dto.setAziendaMadrinaRagioneSociale(aziendaMadrina.getRagioneSociale());
        }
        // Placeholder per futura integrazione con allievi senza esporre entita annidate.
        dto.setAllieviCount(corso.getAllievi() == null ? 0 : corso.getAllievi().size());
        return dto;
    }

    private Corso toEntity(CorsoDTO dto) {
        Corso corso = new Corso();
        corso.setNome(dto.getNome());
        corso.setAnnoAccademico(dto.getAnnoAccademico());
        corso.setStato(dto.getStato());
        return corso;
    }

}
