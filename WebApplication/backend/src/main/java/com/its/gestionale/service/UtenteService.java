package com.its.gestionale.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.UtenteDTO;
import com.its.gestionale.entity.Utente;
import com.its.gestionale.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    // Restituisce tutti gli utenti come DTO
    public List<UtenteDTO> findAll() {
        List<Utente> utenti = utenteRepository.findAll();
        List<UtenteDTO> dtos = new ArrayList<>();

        for (Utente utente : utenti) {
            dtos.add(UtenteDTO.fromEntity(utente));
        }

        return dtos;
    }

    // Trova un utente per username — utile per login
    public UtenteDTO findByUsername(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utente con username " + username + " non trovato"
                ));

        return UtenteDTO.fromEntity(utente);
    }

    // Crea un nuovo utente
    public UtenteDTO save(UtenteDTO dto) {
        // ↓ Controlla che username non sia già in uso
        if (utenteRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username " + dto.getUsername() + " è già in uso"
            );
        }

        Utente utente = UtenteDTO.toEntity(dto);
        // ↓ NOTA: In produzione, hashare la password con bcrypt o Argon2
        // Per ora usiamo la password in plain text — SOLO PER SVILUPPO!
        utente.setAttivo(true);

        return UtenteDTO.fromEntity(utenteRepository.save(utente));
    }

    // Aggiorna un utente esistente
    public UtenteDTO update(Long id, UtenteDTO dto) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utente con id " + id + " non trovato"
                ));

        utente.setEmail(dto.getEmail());
        utente.setNome(dto.getNome());
        utente.setCognome(dto.getCognome());
        utente.setRuolo(Utente.RuoloUtente.valueOf(dto.getRuolo()));
        utente.setAttivo(dto.getAttivo());

        return UtenteDTO.fromEntity(utenteRepository.save(utente));
    }

    // Disattiva un utente (soft delete — non lo cancella, solo lo marca come inattivo)
    public void disattivaUtente(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utente con id " + id + " non trovato"
                ));

        utente.setAttivo(false);
        utenteRepository.save(utente);
    }
}
