package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Role;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.repo.UserRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactImportServiceImplTest {

    @Mock
    ContactRepo contactRepo;

    @Mock
    UserRepo userRepo;

    @InjectMocks
    ContactImportServiceImpl contactService;

    @Test
    void importContacts_Success() throws IOException {

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@email.com");
        testUser.setRole(Role.ROLE_USER);
        testUser.setPassword("12345678");

        userRepo.save(testUser);

        String csvContent = "familyNameEng,firstNameEng,familyNameCh,firstNameCh,mobilePhone,email,birthdayReminder,birthDate\n" +
                "Wayne,Bruce,Wei,An,999888777,bruce@waynecorp.com,true,1939-05-27";
        MockMultipartFile file = new MockMultipartFile("file", "contacts.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        ArgumentCaptor<Iterable<Contact>> captor = ArgumentCaptor.forClass(Iterable.class);

        when(userRepo.findByUsername("testUser")).thenReturn(java.util.Optional.of(testUser));

        contactService.importContacts(file, "testUser");

        verify(contactRepo).saveAll(captor.capture()); // Capturamos lo que se envió al repo

        List<Contact> resultList = (List<Contact>) captor.getValue();

        assertEquals(1, resultList.size());
        Contact c = resultList.get(0);
        assertEquals("Bruce", c.getFirstNameEng());
        assertEquals(LocalDate.of(1939, 5, 27), c.getBirthDate());
        assertNull(c.getId());
        }

    @Test
    void importContacts_WrongExtension_ThrowsException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", new byte[10]);

        assertThrows(IllegalArgumentException.class, () -> contactService.importContacts(file, "testUser"));
    }

    @Test
    void importContacts_InvalidExtension_ThrowsException() {

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@email.com");
        testUser.setRole(Role.ROLE_USER);
        testUser.setPassword("12345678");

        userRepo.save(testUser);

        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "fake-data".getBytes());

        when(userRepo.findByUsername("testUser")).thenReturn(java.util.Optional.of(testUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                contactService.importContacts(file, "testUser")
        );
        assertEquals("Only CSV files are allowed", ex.getMessage());
    }
}