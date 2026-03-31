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

@Service
public class AuthService {

    private final UtenteRepository utenteRepository;

    public AuthService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    // Flusso login semplificato per sviluppo:
    // 1) recupero utente per email
    // 2) verifico flag attivo
    // 3) confronto password "as-is" (no hashing in questa fase)
    // 4) aggiorno metadata ultimo accesso
    // 5) restituisco token leggero (email), compatibile con il frontend attuale
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

        // Token intentionally semplice:
        // il frontend si aspetta un campo token e continua a inviarlo come Bearer.
        // In questo profilo, token=email minimizza complessita durante sviluppo.
        return new LoginResponse(utente.getEmail(), "Bearer", 0);
    }

    // Parsing centralizzato dell'header Authorization per endpoint /auth/me.
    // Manteniamo il contratto Bearer per non cambiare interceptor lato Angular.
    public MeResponse meFromAuthorization(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header mancante o non valido");
        }

        String email = authorizationHeader.substring(7).trim();
        if (email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido");
        }

        return me(email);
    }

    // Mapping utente -> risposta profilo usata dalla navbar/dashboard.
    // In caso di token non coerente con utente esistente, ritorniamo 404.
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
