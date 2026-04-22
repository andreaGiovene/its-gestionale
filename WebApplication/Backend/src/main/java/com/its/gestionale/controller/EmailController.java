package com.its.gestionale.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.its.gestionale.dto.EmailMonitoraggioDTO;
import com.its.gestionale.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Invia email di monitoraggio all'azienda.
     */
    @PostMapping("/monitoraggio")
    public ResponseEntity<String> inviaEmailMonitoraggio(
            @RequestBody EmailMonitoraggioDTO body) {

        String subject = "Monitoraggio tirocinio programmato";

        String text = """
                Gentile azienda,

                con la presente si comunica che è stato programmato un monitoraggio del tirocinio.

                Il monitoraggio riguarda l’allievo Mario Rossi.

                Il nostro referente si recherà presso la vostra sede nella data concordata.

                Cordiali saluti,
                ITS Job Placement
                """.formatted(body.getTirocinioId());

        emailService.sendEmail(
                body.getEmailDestinatario(),
                subject,
                text
        );

        return ResponseEntity.ok("Email inviata con successo");
    }
}