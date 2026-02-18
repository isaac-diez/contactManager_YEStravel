package com.yestravel.contactsmanager.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableRetry
@EnableAsync
class EmailServiceRetryTest {

    @MockBean
    private JavaMailSender mailSender;

    @SpyBean
    private EmailService emailService;

    @Test
    void testSendBirthdayReminderEmail() {

        String recipient = "mockRecipient@email.com";
        String contactName = "mockContactName";

        doThrow(new MailSendException("Simulated connection error"))
            .when(mailSender)
            .send(any(SimpleMailMessage.class));

        try {
            emailService.sendBirthdayReminderEmail(recipient, contactName);
        } catch (Exception e) {
        }

        org.awaitility.Awaitility.await()
                        .atMost(Duration.ofSeconds(15))
                        .untilAsserted(() -> {
                            verify(mailSender, times(3)).send(any(SimpleMailMessage.class));
                            verify(emailService, times(1)).recoverEmail(
                                    any(MailSendException.class),
                                    eq(recipient),
                                    eq(contactName)
                            );
                        });
    }

}
