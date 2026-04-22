package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

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
    // Usa la query custom findAllWithCorso() per pre-caricare il Corso e
    // evitare query aggiuntive durante il mapping Entity -> DTO.
    // Complessita' temporale: O(n), dove n = numero di record letti dal DB.
    // Complessita' spaziale: O(n), per la nuova lista di DTO creata in output.
    // Costo nascosto: findAll() carica tutto in memoria; su tabelle grandi e' meglio paginare.

    @Transactional(readOnly = true)  // Apre una transazione di sola lettura: evita flush/modifiche non volute e puo ottimizzare le query lato JPA/DB.
    public List<AllievoDTO> findAll() {
        List<Allievo> allievi = allievoRepository.findAllWithCorso();
        List<AllievoDTO> dtos = new ArrayList<>();

        for (Allievo allievo : allievi) {
            dtos.add(AllievoDTO.fromEntity(allievo));
        }

        return dtos;
    }

    // Restituisce gli allievi filtrati dal testo digitato dall'utente.
    // Se la stringa e' vuota, mantiene il fallback sull'elenco completo.
    // La logica SQL di match parziale resta nel repository, non nel service.
    @Transactional(readOnly = true)
    public List<AllievoDTO> search(String search) {
        if (!StringUtils.hasText(search)) {
            return findAll();
        }

        List<Allievo> allievi = allievoRepository.searchByText(search.trim());
        List<AllievoDTO> dtos = new ArrayList<>();

        for (Allievo allievo : allievi) {
            dtos.add(AllievoDTO.fromEntity(allievo));
        }

        return dtos;
    }

    // Restituisce gli allievi di un corso specifico.
    // Rimane un filtro dedicato separato dalla ricerca testuale trasversale.
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

        if (allievoRepository.existsByCodiceFiscale(dto.getCodiceFiscale())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Allievo con questo codice fiscale esiste già"
            );
        }
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

    // Restituisce tutti gli allievi senza tirocinio associato
    @Transactional(readOnly = true)
    public List<AllievoDTO> getAllieviSenzaStage() {
        List<Allievo> allievi = allievoRepository.findByTirociniIsEmpty();
        List<AllievoDTO> dtos = new ArrayList<>();

        for (Allievo allievo : allievi) {
            dtos.add(AllievoDTO.fromEntity(allievo));
        }

        return dtos;
    }

    // Elimina un allievo
    public void deleteById(Integer id) {
        if (!allievoRepository.existsById(id)) {
            throw new AllievoNotFoundException(id);
        }
        allievoRepository.deleteById(id);
    }
}