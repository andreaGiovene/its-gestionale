package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.AllievoDTO;
import com.its.gestionale.entity.Allievo;
import com.its.gestionale.entity.Corso;
import com.its.gestionale.exception.AllievoNotFoundException;
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

    @Transactional(readOnly = true)  // Apre una transazione di sola lettura: evita flush/modifiche non volute e puo ottimizzare le query lato JPA/DB.
    public List<AllievoDTO> findAll() {
        List<Allievo> allievi = allievoRepository.findAll();
        List<AllievoDTO> dtos = new ArrayList<>();

        for (Allievo allievo : allievi) {
            dtos.add(AllievoDTO.fromEntity(allievo));
        }

        return dtos;
    }

    // Restituisce gli allievi di un corso specifico
    @Transactional(readOnly = true)
    public List<AllievoDTO> findByCorsoId(Integer corsoId) {
        List<Allievo> allievi = allievoRepository.findByCorsoId(corsoId);
        List<AllievoDTO> dtos = new ArrayList<>();

        for (Allievo allievo : allievi) {
            dtos.add(AllievoDTO.fromEntity(allievo));
        }

        return dtos;
    }

    // Trova un allievo per ID
    @Transactional(readOnly = true)
    public AllievoDTO findById(Integer id) {
        Allievo allievo = allievoRepository.findById(id)
                .orElseThrow(() -> new AllievoNotFoundException(id));
        return AllievoDTO.fromEntity(allievo);
    }

    // Crea un nuovo allievo
    public AllievoDTO save(AllievoDTO dto) {
        Allievo allievo = new Allievo();
        allievo.setNome(dto.getNome());
        allievo.setCognome(dto.getCognome());
        extracted(dto, allievo);
        allievo.setCodiceFiscale(dto.getCodiceFiscale());
        allievo.setDataDiNascita(dto.getDataDiNascita());
        allievo.setNote(dto.getNote());

        if (dto.getCorsoId() != null) {
            Corso corso = corsoRepository.findById(dto.getCorsoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Corso con id " + dto.getCorsoId() + " non trovato"
                    ));
            allievo.setCorso(corso);
        }

        return AllievoDTO.fromEntity(allievoRepository.save(allievo));
    }

    // Aggiorna un allievo esistente
    public AllievoDTO update(Integer id, AllievoDTO dto) {
        Allievo allievo = allievoRepository.findById(id)
                .orElseThrow(() -> new AllievoNotFoundException(id));

        allievo.setNome(dto.getNome());
        allievo.setCognome(dto.getCognome());
        extracted(dto, allievo);
        allievo.setCodiceFiscale(dto.getCodiceFiscale());
        allievo.setDataDiNascita(dto.getDataDiNascita());
        allievo.setNote(dto.getNote());

        if (dto.getCorsoId() != null) {
            Corso corso = corsoRepository.findById(dto.getCorsoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Corso con id " + dto.getCorsoId() + " non trovato"
                    ));
            allievo.setCorso(corso);
        }

        return AllievoDTO.fromEntity(allievoRepository.save(allievo));
    }

    private void extracted(AllievoDTO dto, Allievo allievo) {
        allievo.setTelefono(dto.getTelefono());
    }

    // Elimina un allievo
    public void deleteById(Integer id) {
        if (!allievoRepository.existsById(id)) {
            throw new AllievoNotFoundException(id);
        }
        allievoRepository.deleteById(id);
    }
}