package com.yestravel.contactsmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailServiceImpl;

    @Test
    void sendBirthdayReminderEmail() {

        String recipient = "mockemail@email.com";
        String contactName = "mockContactName";

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendBirthdayReminderEmail(recipient, contactName);

        verify(emailServiceImpl).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getTo()).contains(recipient);
        assertThat(sentMessage.getFrom()).isEqualTo("noreply@yestravel.com");
        assertThat(sentMessage.getSubject()).contains("Birthday reminder:");
        assertThat(sentMessage.getSubject()).contains(contactName);
        assertThat(sentMessage.getText()).contains(contactName);
        assertThat(sentMessage.getText()).contains("Hi,\n\nThe birthday of " + contactName + " is coming up.");

    }
}