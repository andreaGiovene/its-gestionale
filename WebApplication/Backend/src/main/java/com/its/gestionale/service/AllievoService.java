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
    @Transactional(readOnly = true)
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

    // Crea un nuovo allievo
    public AllievoDTO save(AllievoDTO dto) {
        Allievo allievo = new Allievo();
        allievo.setNome(dto.getNome());
        allievo.setCognome(dto.getCognome());
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
}