package com.yestravel.contactsmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Retryable(
            retryFor = { MailException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000, multiplier = 2)
    )
    @Async // Importante: Para no bloquear el hilo del scheduler
    public void sendBirthdayReminderEmail(String to, String contactName) {
        log.info("Trying to send reminder to {} (Thread: {})", to, Thread.currentThread().getName());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@yestravel.com");
        message.setTo(to);
        message.setSubject("🎂 Birthday reminder: " + contactName);
        message.setText("Hi,\n\nThe birthday of " + contactName +
                " is coming up. Great moment to contact them!\n\nRegards.");

        mailSender.send(message);
        log.info("Mail successfully sent to: {}", to);
    }

    @Recover
    public void recoverEmail(MailException e, String to, String contactName) {
        log.error("DEFINITIVE FAILURE: Email couldn't be sent to {} after 3 tries. Error: {}", to, e.getMessage());
        // Aquí podrías guardar el fallo en una tabla de auditoría en la BD
    }
}