package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.dto.ContactFormDTO;
import com.yestravel.contactsmanager.mapping.ContactMapperImpl;
import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepo contactRepo;

    @Mock
    private UserRepo userRepo;

    @Spy // Usamos Spy con una instancia real para que el mapeo funcione de verdad
    private ContactMapperImpl contactMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    @InjectMocks
    private UserService userService;

    @Test
    void getContacts() {

        Contact c1 = new Contact();
        Contact c2 = new Contact();
        Contact c3 = new Contact();
        c1.setId(1L); c2.setId(2L); c3.setId(3L);
        c1.setFirstNameEng("john"); c2.setFirstNameEng("lucy"); c3.setFirstNameEng("jill");
        List<Contact> list = List.of(c1,c2,c3);
        Page<Contact> mockPage = new PageImpl<>(list, PageRequest.of(0, 10), 3);

        when(contactRepo.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<Contact> result = contactService.getContacts(0, 10);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(list, result.getContent());
        assertEquals("john", result.getContent().get(0).getFirstNameEng());
        assertEquals("lucy", result.getContent().get(1).getFirstNameEng());
        assertEquals("jill", result.getContent().get(2).getFirstNameEng());

    }

    @Test
    void getContactById() {

        Contact mockContact = new Contact();
        mockContact.setId(3L);
        mockContact.setFirstNameEng("John");
        when(contactRepo.findById(3L)).thenReturn(Optional.of(mockContact));

        Contact result = contactService.getContactById(mockContact.getId());

        assertNotNull(result);
        assertEquals(mockContact.getId(), result.getId());
        assertEquals(mockContact.getFirstNameEng(), result.getFirstNameEng());

    }

    @Test
    void getContactById_NotFound() {
        when(contactRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> contactService.getContactById(1L));
    }

    @Test
    void getBirthdayContacts() {

        Contact c1 = new Contact();
        Contact c2 = new Contact();
        Contact c3 = new Contact();
        c1.setId(1L); c2.setId(2L); c3.setId(3L);
        c1.setBirthDate(LocalDate.of(2000, 12, 19));
        c2.setBirthDate(LocalDate.of(2000, 5, 18));
        c3.setBirthDate(LocalDate.of(2000, 1, 17));
        List<Contact> list = List.of(c1,c2,c3);
        Page<Contact> mockPage = new PageImpl<>(list, PageRequest.of(0, 10), 3);

        when(contactRepo.findUpcomingBirthdays(any(PageRequest.class))).thenReturn(mockPage);

        Page<Contact> result = contactService.getBirthdayContacts(0, 10);

        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getId());
        assertEquals(2, result.getContent().get(1).getId());
        assertEquals(3, result.getContent().get(2).getId());
    }

    @Test
    void findById() {
        Contact c = new Contact();
        c.setId(1L);
        c.setFirstNameEng("John");

        when(contactRepo.findById(1L)).thenReturn(Optional.of(c));

        Contact result = contactService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstNameEng());
    }

    @Test
    void saveContact() {
        ContactFormDTO newContact = new ContactFormDTO();
        newContact.setFirstNameEng("John");
        newContact.setEmail("john@example.com");
        newContact.setMobilePhone("123456789");
        newContact.setBirthDate(LocalDate.of(2000, 12, 19));

        Contact savedContact = new Contact();
        savedContact.setId(50L);
        savedContact.setFirstNameEng("John");
        savedContact.setEmail("john@example.com");
        savedContact.setMobilePhone("123456789");
        savedContact.setBirthDate(LocalDate.of(2000, 12, 19));

        User testUser = new User();
        testUser.setUsername("testUser");

        when(contactRepo.save(any(Contact.class))).thenReturn(savedContact);
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));

        Contact result = contactService.saveContact(newContact, testUser.getUsername(), true);

        assertNotNull(result);
        assertEquals(50L, result.getId());
        assertEquals(savedContact.getFirstNameEng(), result.getFirstNameEng());
        assertEquals(savedContact.getEmail(), result.getEmail());
        assertEquals(savedContact.getMobilePhone(), result.getMobilePhone());
        assertEquals(savedContact.getBirthDate(), result.getBirthDate());

    }

    @Test
    void deleteContact() {

        Contact newContact = new Contact();
        newContact.setFirstNameEng("To Delete");

        contactService.deleteContact(newContact);

        verify(contactRepo, times(1)).delete(newContact);
    }
}