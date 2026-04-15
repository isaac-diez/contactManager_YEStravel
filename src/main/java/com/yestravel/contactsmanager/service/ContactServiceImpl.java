package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.controller.ContactController;
import com.yestravel.contactsmanager.dto.ContactFormDTO;
import com.yestravel.contactsmanager.mapping.ContactMapperImpl;
import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Role;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);


    private final ContactRepo contactRepo;
    private final UserRepo userRepo;
    private final ContactMapperImpl contactMapper;


    public ContactServiceImpl(ContactRepo contactRepo, UserRepo userRepo, ContactMapperImpl contactMapper) {
        this.contactRepo = contactRepo;
        this.userRepo = userRepo;
        this.contactMapper = contactMapper;
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
    public Contact saveContact(ContactFormDTO dto, String userName, boolean isAdmin) throws UsernameNotFoundException {

        Contact contact;

        if (dto.getId() != null) {
            contact = contactRepo.findById(dto.getId()).
                    orElseThrow(() -> new RuntimeException("Contact not found with ID: " + dto.getId()));
            contactMapper.UpdateContactFromDto(dto, contact);
        } else {
            contact = contactMapper.toEntity(dto);
            logger.info("New Contact received from FORM: " + contact);

        }

        if (isAdmin && dto.getUserId() != null) {
            User newOwner = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            contact.setUser(newOwner);
            logger.info("New owner received from FORM: " + newOwner.getUsername());

        } else if (contact.getUser() == null) {
            User user = userRepo.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + userName));
            contact.setUser(user);
            logger.info("New Contact received from FORM: " + contact);

        }

        return contactRepo.save(contact);

    }

    @Override
    public void deleteContact(Contact contact) {

        contactRepo.delete(contact);

    }

    @Override
    public boolean isAdmin(String userName) {
        User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
        return user.getRole().equals(Role.ROLE_ADMIN);
    }
}
