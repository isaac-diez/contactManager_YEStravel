package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ContactService {

    public Page<Contact> getContacts(int pageNumber, int pageSize);

    public Contact getContactById(Long id);

    public Page<Contact> getBirthdayContacts(int pageNumber, int pageSize);

    public Contact findById(Long id);

    Contact saveContact(Contact contact);

    public void deleteContact(Contact contact);

}
