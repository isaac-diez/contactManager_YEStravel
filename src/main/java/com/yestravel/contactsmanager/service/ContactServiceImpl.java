package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.repo.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

//    @Override
//    public List<Contact> listContact() {
//
//        return contactRepo.findAll();


    @Override
    public Page<Contact> getContacts(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return contactRepo.findAll(pageRequest);
    }

    @Override
    public Page<Contact> getBirthdayContacts(int pageNumber, int pageSize){

        int nextMonth = (LocalDate.now().getMonthValue() % 12) + 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("birthDate"));
        return contactRepo.findByBirthDateMonth(nextMonth, pageRequest);
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
