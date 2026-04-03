package com.its.gestionale.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.UtenteDTO;
import com.its.gestionale.entity.Ruolo;
import com.its.gestionale.entity.Utente;
import com.its.gestionale.repository.RuoloRepository;
import com.its.gestionale.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final RuoloRepository ruoloRepository;

    public UtenteService(UtenteRepository utenteRepository, RuoloRepository ruoloRepository) {
        this.utenteRepository = utenteRepository;
        this.ruoloRepository = ruoloRepository;
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

    // Trova un utente per email
    public UtenteDTO findByEmail(String email) {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utente con email " + email + " non trovato"
                ));

        return UtenteDTO.fromEntity(utente);
    }

    // Crea un nuovo utente
    public UtenteDTO save(UtenteDTO dto) {
        // ↓ Controlla che email non sia già in uso
        if (utenteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email " + dto.getEmail() + " è già in uso"
            );
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password obbligatoria"
            );
        }

        Ruolo ruolo = findRuoloByCodice(dto.getRuolo());
        LocalDateTime now = LocalDateTime.now();

        Utente utente = new Utente();
        utente.setEmail(dto.getEmail());
        utente.setPasswordHash(dto.getPassword());
        utente.setRuolo(ruolo);
        utente.setAttivo(dto.getAttivo() != null ? dto.getAttivo() : Boolean.TRUE);
        utente.setCreatoIl(now);
        utente.setAggiornatoIl(now);

        return UtenteDTO.fromEntity(utenteRepository.save(utente));
    }

    // Aggiorna un utente esistente
    public UtenteDTO update(Integer id, UtenteDTO dto) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utente con id " + id + " non trovato"
                ));

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(utente.getEmail())
            && utenteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email " + dto.getEmail() + " è già in uso"
            );
        }

        utente.setEmail(dto.getEmail());
        utente.setRuolo(findRuoloByCodice(dto.getRuolo()));
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            utente.setPasswordHash(dto.getPassword());
        }
        utente.setAttivo(dto.getAttivo());
        utente.setAggiornatoIl(LocalDateTime.now());

        return UtenteDTO.fromEntity(utenteRepository.save(utente));
    }

    // Disattiva un utente (soft delete — non lo cancella, solo lo marca come inattivo)
    public void disattivaUtente(Integer id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utente con id " + id + " non trovato"
                ));

        utente.setAttivo(false);
        utente.setAggiornatoIl(LocalDateTime.now());
        utenteRepository.save(utente);
    }

    private Ruolo findRuoloByCodice(String codice) {
        if (codice == null || codice.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ruolo obbligatorio"
            );
        }

        return ruoloRepository.findByCodice(codice)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ruolo " + codice + " non trovato"
                ));
    }
}
