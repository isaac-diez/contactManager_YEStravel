package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.repo.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactServiceImpl implements IContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public List<Contact> listContact() {

        return contactRepo.findAll();

    }

    @Override
    public List<Contact> birthdayListContact(){

        int nextMonth = (LocalDate.now().getMonthValue() % 12) + 1;
        return contactRepo.findAll().stream()
                .filter(c -> c.getBirthDate() != null &&
                        c.getBirthDate().getMonthValue() == nextMonth)
                .toList();
    }

    @Override
    public Contact findContactById(Integer contactId) {

        return contactRepo.findById(contactId).orElse(null);

        //TODO: create exception

    }

    @Override
    public void saveContact(Contact contact) {

        contactRepo.save(contact);

    }

    @Override
    public void deleteContact(Contact contact) {

        contactRepo.delete(contact);

    }
}
