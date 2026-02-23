package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.dto.ContactFormDTO;
import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;

public interface ContactService {

    public Page<Contact> getContacts(int pageNumber, int pageSize);

    public Contact getContactById(Long id);

    public Page<Contact> getBirthdayContacts(int pageNumber, int pageSize);

    public Contact findById(Long id);

    Contact saveContact(ContactFormDTO dto, String userName, boolean isAdmin);

    public void deleteContact(Contact contact);

    public boolean isAdmin(String userName);

}
