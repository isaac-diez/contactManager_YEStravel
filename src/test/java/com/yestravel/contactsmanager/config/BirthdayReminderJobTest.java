package com.yestravel.contactsmanager.config;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdayReminderJobTest {

    @Mock
    private ContactRepo contactRepo;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BirthdayReminderJob birthdayReminderJob;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(birthdayReminderJob, "daysBefore", 7);
    }

    @Test
    void processBirthdayReminders_Success() {

        LocalDate targetDate = LocalDate.now().plusDays(7);

        User owner = new User();
        owner.setEmail("mockEmail@email.com");

        Contact contact = new Contact();
        contact.setFirstNameEng("Sandy");
        contact.setFamilyNameEng("Li");
        contact.setUser(owner);

        when(contactRepo.findBirthdayReminderAndMonthAndDay(
                targetDate.getMonthValue(),
                targetDate.getDayOfMonth()))
                .thenReturn(List.of(contact));

        birthdayReminderJob.processBirthdayReminders();

        verify(contactRepo, times(1)).findBirthdayReminderAndMonthAndDay(
                targetDate.getMonthValue(),
                targetDate.getDayOfMonth()
        );

        verify(emailService, times(1)).sendBirthdayReminderEmail(
                eq("mockEmail@email.com"),
                contains("Sandy Li")
        );

    }
}