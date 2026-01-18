package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.repo.ContactRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepo contactRepo;

    public ContactServiceImpl(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    @Override
    public Page<Contact> getContacts(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return contactRepo.findAll(pageRequest);
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @Override
    public Page<Contact> getBirthdayContacts(int pageNumber, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return contactRepo.findUpcomingBirthdays(pageRequest);
    }

    @Override
    public Contact findById(Long contactId) {

        return contactRepo.findById(contactId).orElseThrow(() -> new RuntimeException("Contact not found"));

        //TODO: create exception

    }

    @Override
    public Contact saveContact(Contact contact) {

        return contactRepo.save(contact);

    }

    @Override
    public void deleteContact(Contact contact) {

        contactRepo.delete(contact);

    }
}
