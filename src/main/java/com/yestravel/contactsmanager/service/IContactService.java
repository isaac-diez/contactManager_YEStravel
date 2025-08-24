package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;

import java.util.List;

public interface IContactService {

    public List<Contact> listContact();

    public List<Contact> birthdayListContact();

    public Contact findContactById(Integer contactId);

    public void saveContact(Contact contact);

    public void deleteContact(Contact contact);

}
