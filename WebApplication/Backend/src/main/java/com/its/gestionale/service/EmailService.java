package com.its.gestionale.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.its.gestionale.entity.EmailLog;
import com.its.gestionale.repository.EmailLogRepository;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final EmailLogRepository emailLogRepository;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider,
                        EmailLogRepository emailLogRepository) {
        this.mailSenderProvider = mailSenderProvider;
        this.emailLogRepository = emailLogRepository;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom("ITS Gestionale <noreply@its-gestionale.local>");

        EmailLog log = new EmailLog();
        log.setDestinatario(to);
        log.setOggetto(subject);
        log.setTesto(text);
        log.setDataInvio(LocalDateTime.now());

        emailLogRepository.save(log);

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            log.warn("JavaMailSender non configurato: email salvata nello storico ma non inviata a {}", to);
            return;
        }

        mailSender.send(msg);
    }
}