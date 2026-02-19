package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Role;
import com.yestravel.contactsmanager.model.User;
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

    @Autowired
    private UserRepo userRepo;

    @Test
    @DisplayName("Should save a contact and generate an ID")
    void saveContact_PersistSuccessfully() {

        User owner = new User();
        owner.setUsername("testuser");
        owner.setEmail("mockEmail@email.com");
        owner.setRole(Role.ROLE_ADMIN);
        owner.setPassword("1234");

        entityManager.persist(owner);

        Contact contact = new Contact();
        contact.setFirstNameEng("John");
        contact.setFamilyNameEng("Doe");
        contact.setFirstNameCh("Tie");
        contact.setFamilyNameCh("Ren");
        contact.setMobilePhone("123456789");
        contact.setEmail("john.doe@example.com");
        contact.setBirthDate(LocalDate.of(1990, 1, 1));
        contact.setUser(owner);

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

        User owner = new User();
        owner.setUsername("testuser");
        owner.setEmail("mockEmail@email.com");
        owner.setRole(Role.ROLE_ADMIN);
        owner.setPassword("1234");

        entityManager.persist(owner);

        Contact contact = new Contact();
        contact.setFirstNameEng("John");
        contact.setFamilyNameEng("Doe");
        contact.setBirthdayReminder(false);
        contact.setUser(owner);

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

        User owner = new User();
        owner.setUsername("testuser");
        owner.setEmail("mockEmail@email.com");
        owner.setRole(Role.ROLE_ADMIN);
        owner.setPassword("1234");

        entityManager.persist(owner);

        Contact contact = new Contact();
        contact.setFirstNameCh("张三");
        contact.setFamilyNameCh("李四");
        contact.setUser(owner);

        Contact chineseContact = contactRepo.save(contact);

        entityManager.flush();
        entityManager.clear();

        Contact retrievedContact = contactRepo.findById(chineseContact.getId()).orElse(null);

        assertNotNull(retrievedContact);
        assertThat(retrievedContact.getFirstNameCh()).isEqualTo("张三");
        assertThat(retrievedContact.getFamilyNameCh()).isEqualTo("李四");

    }


}