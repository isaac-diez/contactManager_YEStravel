package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.repo.ContactRepo;
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

@ExtendWith(MockitoExtension.class)
class ContactImportServiceImplTest {

    @Mock
    ContactRepo contactRepo;

    @InjectMocks
    ContactImportServiceImpl contactService;

    @Test
    void importContacts_Success() throws IOException {
        String csvContent = "familyNameEng,firstNameEng,familyNameCh,firstNameCh,mobilePhone,email,birthDate\n" +
                "Wayne,Bruce,Wei,An,999888777,bruce@waynecorp.com,1939-05-27";
        MockMultipartFile file = new MockMultipartFile("file", "contacts.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        ArgumentCaptor<Iterable<Contact>> captor = ArgumentCaptor.forClass(Iterable.class);

        contactService.importContacts(file);

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

        assertThrows(IllegalArgumentException.class, () -> contactService.importContacts(file));
    }

    @Test
    void importContacts_InvalidExtension_ThrowsException() {
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "fake-data".getBytes());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                contactService.importContacts(file)
        );
        assertEquals("Only CSV files are allowed", ex.getMessage());
    }
}