package com.yestravel.contactsmanager.config;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BirthdayReminderJob {

    private final ContactRepo contactRepo;
    private final EmailService emailService;

    // Configurable en properties. Ej: 7 días antes
    @Value("${app.reminders.days-before:7}")
    private int daysBefore;

    // Se ejecuta todos los días a las 09:00 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void processBirthdayReminders() {
        log.info("Iniciando job de recordatorios de cumpleaños...");

        // 1. Calculamos la fecha objetivo (Hoy + X días)
        LocalDate targetDate = LocalDate.now().plusDays(daysBefore);
        int targetMonth = targetDate.getMonthValue();
        int targetDay = targetDate.getDayOfMonth();

        // 2. Buscamos en BD
        List<Contact> contactsToRemind = contactRepo.findBirthdayReminderAndMonthAndDay(targetMonth, targetDay);

        // 3. Enviamos correos
        for (Contact contact : contactsToRemind) {
            // Asumiendo que Contact tiene relación con User (owner)
            if (contact.getUser() != null && contact.getUser().getEmail() != null) {
                emailService.sendBirthdayReminderEmail(
                        contact.getUser().getEmail(),
                        contact.getFirstNameEng() + " " + contact.getFamilyNameEng()
                );
            }
        }

        log.info("Recordatorios enviados: {}", contactsToRemind.size());
    }
}