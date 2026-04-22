package com.its.gestionale.service;

import java.time.LocalDateTime;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.its.gestionale.entity.EmailLog;
import com.its.gestionale.repository.EmailLogRepository;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public EmailService(JavaMailSender mailSender,
                        EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom("LA_TUA_EMAIL@gmail.com");

        EmailLog log = new EmailLog();
        log.setDestinatario(to);
        log.setOggetto(subject);
        log.setTesto(text);
        log.setDataInvio(LocalDateTime.now());

        emailLogRepository.save(log);

        // nome "ITS" visibile + mittente reale Gmail
        msg.setFrom("ITS Gestionale <LA_TUA_EMAIL@gmail.com>");

        mailSender.send(msg);
    }
}