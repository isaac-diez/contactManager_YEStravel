package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;

public interface ContactService {

//    public List<Contact> listContact();

    public Page<Contact> getContacts(int pageNumber, int pageSize);

    public Page<Contact> getBirthdayContacts(int pageNumber, int pageSize);

    public Contact findContactById(Integer contactId);

    public void saveContact(Contact contact);

    public void deleteContact(Contact contact);

}
