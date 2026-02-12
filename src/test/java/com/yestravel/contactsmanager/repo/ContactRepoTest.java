package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Contact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ContactRepoTest {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should save a contact and generate an ID")
    void saveContact_PersistSuccessfully() {

        Contact contact = new Contact();
        contact.setFirstNameEng("John");
        contact.setFamilyNameEng("Doe");
        contact.setFirstNameCh("Tie");
        contact.setFamilyNameCh("Ren");
        contact.setMobilePhone("123456789");
        contact.setEmail("john.doe@example.com");
        contact.setBirthDate(LocalDate.of(1990, 1, 1));

        Contact savedContact = contactRepo.save(contact);

        entityManager.flush();
        entityManager.clear();

        Contact retrievedContact = contactRepo.findById(savedContact.getId()).orElse(null);

        assertNotNull(retrievedContact);
        assertThat(retrievedContact.getId()).isNotNull();
        assertThat(retrievedContact.getFirstNameEng()).isEqualTo("John");
        assertThat(retrievedContact.isBirthdayReminder()).isTrue();

    }

    @Test
    @DisplayName("Should persist birthdayReminder as false when explicitly set")
    void saveContact_WithReminderFalse() {

        Contact contact = new Contact();
        contact.setFirstNameEng("Peter");
        contact.setFamilyNameEng("Parker");
        contact.setBirthdayReminder(false);

        Contact savedContact = contactRepo.save(contact);

        entityManager.flush();
        entityManager.clear();

        Contact retrievedContact = contactRepo.findById(savedContact.getId()).orElse(null);

        assertNotNull(retrievedContact);
        assertThat(retrievedContact.isBirthdayReminder()).isFalse();

    }

    @Test
    @DisplayName("Should handle chinese chracters correctly")
    void saveContact_StoresUtf8Characters() {

        Contact contact = new Contact();
        contact.setFirstNameCh("张三");
        contact.setFamilyNameCh("李四");

        Contact chineseContact = contactRepo.save(contact);

        entityManager.flush();
        entityManager.clear();

        Contact retrievedContact = contactRepo.findById(chineseContact.getId()).orElse(null);

        assertNotNull(retrievedContact);
        assertThat(retrievedContact.getFirstNameCh()).isEqualTo("张三");
        assertThat(retrievedContact.getFamilyNameCh()).isEqualTo("李四");

    }


}