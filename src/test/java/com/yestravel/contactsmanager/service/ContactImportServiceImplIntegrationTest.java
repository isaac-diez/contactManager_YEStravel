package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Role;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ContactImportServiceImplIntegrationTest {

    @Autowired
    private ContactImportServiceImpl contactService;

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    @DisplayName("Integration: Should persist CSV data into H2 Database")
    void importContacts_DatabaseIntegration() throws IOException {

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@email.com");
        testUser.setRole(Role.ROLE_USER);
        testUser.setPassword("12345678");

        // 1. Arrange: Leemos el archivo físico de resources
        ClassPathResource res = new ClassPathResource("test-ContactsForIntegrationTest.csv");
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-ContactsForIntegrationTest.csv", "text/csv", res.getInputStream());

        long countBefore = contactRepo.count();

        // 2. Act
        userRepo.save(testUser);
        contactService.importContacts(file, "testUser");

        contactRepo.findAll().forEach(c ->
                System.out.println("Saved contact: " + c.getFirstNameEng() + " - " + c.getEmail())
        );

        // 3. Assert
        long countAfter = contactRepo.count();
        assertTrue(countAfter > countBefore, "Database should have more records after import");

        // Verificamos que un dato específico del archivo se guardó bien
        Optional<Contact> saved = contactRepo.findAll().stream()
                .filter(c -> c.getEmail().equals("imported@example.com")) // Pon este email en tu CSV de prueba
                .findFirst();

        assertTrue(saved.isPresent());
        assertEquals("Gaelle", saved.get().getFirstNameEng());
    }
}
