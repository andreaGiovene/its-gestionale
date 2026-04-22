package com.its.gestionale.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.its.gestionale.service.EmailService;


import org.springframework.context.annotation.Profile;
@Profile("dev")
@RestController
@RequestMapping("/api/test-email")
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public String test() {

        emailService.sendEmail(
                "maria.cernat@edu-its.it",
                "Test Gestionale ITS",
                "Email di test inviata dal backend 🎯"
        );

        return "Email inviata";
    }
}