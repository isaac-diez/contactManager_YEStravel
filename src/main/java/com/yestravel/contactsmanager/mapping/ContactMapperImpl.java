package com.yestravel.contactsmanager.mapping;

import com.yestravel.contactsmanager.dto.ContactFormDTO;
import com.yestravel.contactsmanager.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public ContactFormDTO toDto(Contact contact) {
        ContactFormDTO dto = new ContactFormDTO();

        dto.setId(contact.getId());
        dto.setFirstNameEng(contact.getFirstNameEng());
        dto.setFamilyNameEng(contact.getFamilyNameEng());
        dto.setFirstNameCh(contact.getFirstNameCh());
        dto.setFamilyNameCh(contact.getFamilyNameCh());
        dto.setEmail(contact.getEmail());
        dto.setMobilePhone(contact.getMobilePhone());
        dto.setEmail(contact.getEmail());
        dto.setBirthDate(contact.getBirthDate());
        dto.setBirthdayReminder(contact.isBirthdayReminder());
        dto.setUserId(contact.getUser().getId());
        dto.setUserName(contact.getUser().getUsername());

        return dto;
    }

    @Override
    public Contact toEntity(ContactFormDTO dto) {
        Contact contact = new Contact();

        contact.setId(dto.getId());
        contact.setFirstNameEng(dto.getFirstNameEng());
        contact.setFamilyNameEng(dto.getFamilyNameEng());
        contact.setFirstNameCh(dto.getFirstNameCh());
        contact.setFamilyNameCh(dto.getFamilyNameCh());
        contact.setEmail(dto.getEmail());
        contact.setMobilePhone(dto.getMobilePhone());
        contact.setEmail(dto.getEmail());
        contact.setBirthDate(dto.getBirthDate());
        contact.setBirthdayReminder(dto.isBirthdayReminder());

        return contact;
    }

    @Override
    public void UpdateContactFromDto(ContactFormDTO dto, Contact contact) {
        contact.setFirstNameEng(dto.getFirstNameEng());
        contact.setFamilyNameEng(dto.getFamilyNameEng());
        contact.setFirstNameCh(dto.getFirstNameCh());
        contact.setFamilyNameCh(dto.getFamilyNameCh());
        contact.setEmail(dto.getEmail());
        contact.setMobilePhone(dto.getMobilePhone());
        contact.setBirthDate(dto.getBirthDate());
        contact.setBirthdayReminder(dto.isBirthdayReminder());
    }
}