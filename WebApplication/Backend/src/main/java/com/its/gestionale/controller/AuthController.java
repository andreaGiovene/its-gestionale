package com.its.gestionale.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.dto.auth.LoginRequest;
import com.its.gestionale.dto.auth.LoginResponse;
import com.its.gestionale.dto.auth.MeResponse;
import com.its.gestionale.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint di login usato dalla schermata frontend.
    // Mantiene contratto classico request/response per ridurre coupling UI/backend.
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Endpoint profilo corrente.
    // Legge direttamente l'header Authorization e delega al service il parsing Bearer.
    // In questo modo il controller resta sottile e tutta la logica di validazione
    // dell'header resta in un solo punto.
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(authService.meFromAuthorization(authorization));
    }
}
