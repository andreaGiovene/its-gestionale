package com.its.gestionale.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.its.gestionale.dto.auth.LoginRequest;
import com.its.gestionale.dto.auth.LoginResponse;
import com.its.gestionale.dto.auth.MeResponse;
import com.its.gestionale.entity.Utente;
import com.its.gestionale.repository.UtenteRepository;
import com.its.gestionale.security.JwtService;

@Service
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final JwtService jwtService;

    public AuthService(UtenteRepository utenteRepository, JwtService jwtService) {
        this.utenteRepository = utenteRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide"));

        if (Boolean.FALSE.equals(utente.getAttivo())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente disattivato");
        }

        if (!utente.getPasswordHash().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide");
        }

        utente.setUltimoAccesso(LocalDateTime.now());
        utente.setAggiornatoIl(LocalDateTime.now());
        utenteRepository.save(utente);

        return new LoginResponse(jwtService.generateToken(utente), "Bearer", jwtService.getExpirationSeconds());
    }

    public MeResponse me(String email) {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato"));

        return new MeResponse(
                utente.getIdUtente(),
                utente.getEmail(),
                utente.getUsername(),
                utente.getRuolo() != null ? utente.getRuolo().getCodice() : null,
                utente.getAttivo());
    }
}
