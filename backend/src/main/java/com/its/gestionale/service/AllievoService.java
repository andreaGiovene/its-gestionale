package com.its.gestionale.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.AllievoDTO;
import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Corso;
import com.its.gestionale.repository.AllievoRepository;
import com.its.gestionale.repository.CorsoRepository;

@Service
public class AllievoService {

    private final AllievoRepository allievoRepository;
    private final CorsoRepository corsoRepository;

    public AllievoService(AllievoRepository allievoRepository,
                          CorsoRepository corsoRepository) {
        this.allievoRepository = allievoRepository;
        this.corsoRepository = corsoRepository;
    }

    // Restituisce tutti gli allievi convertiti in DTO.
    // Complessita' temporale: O(n), dove n = numero di record letti dal DB.
    // Complessita' spaziale: O(n), per la nuova lista di DTO creata in output.
    // Costo nascosto: findAll() carica tutto in memoria; su tabelle grandi e' meglio paginare.
    public List<AllievoDTO> findAll() {
        return allievoRepository.findAll()
                .stream()
                // stream() crea una pipeline di trasformazione (iterazione interna).
                .map(AllievoDTO::fromEntity)
                // map applica fromEntity a ogni elemento: Allievo -> AllievoDTO.
                .collect(Collectors.toList());
                // collect materializza il risultato in una nuova List<AllievoDTO>.
    }

    // Restituisce gli allievi di un corso specifico
    public List<AllievoDTO> findByCorsoId(Long corsoId) {
        return allievoRepository.findByCorsoId(corsoId)
                .stream()
                .map(AllievoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Crea un nuovo allievo
    // Riceve il DTO dalla richiesta HTTP e lo converte in Entity
    public AllievoDTO save(AllievoDTO dto) {
        Allievo allievo = new Allievo();
        allievo.setNome(dto.getNome());
        allievo.setCognome(dto.getCognome());
        allievo.setCodiceFiscale(dto.getCodiceFiscale());
        allievo.setEmail(dto.getEmail());
        allievo.setTelefono(dto.getTelefono());

        // Collega il corso se è stato specificato un corsoId
        if (dto.getCorsoId() != null) {
            Corso corso = corsoRepository.findById(dto.getCorsoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Corso con id " + dto.getCorsoId() + " non trovato"
                    ));
            // ↑ Se il corsoId non esiste → errore 400 Bad Request
            allievo.setCorso(corso);
        }

        return AllievoDTO.fromEntity(allievoRepository.save(allievo));
    }
}