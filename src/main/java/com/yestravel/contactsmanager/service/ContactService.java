package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.dto.ContactFormDTO;
import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {

    public Page<Contact> getContacts(Pageable pageable);

    public Contact getContactById(Long id);

    Page<Contact> searchContacts(String query, Pageable pageable);

    public Page<Contact> getBirthdayContacts(int pageNumber, int pageSize);

    public Contact findById(Long id);

    Contact saveContact(ContactFormDTO dto, String userName, boolean isAdmin);

    public void deleteContact(Contact contact);

    public boolean isAdmin(String userName);

}
