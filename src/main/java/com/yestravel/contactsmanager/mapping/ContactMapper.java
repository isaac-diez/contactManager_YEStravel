package com.yestravel.contactsmanager.mapping;

import com.yestravel.contactsmanager.dto.ContactFormDTO;
import com.yestravel.contactsmanager.model.Contact;

public interface ContactMapper {

    ContactFormDTO toDto(Contact contact);

    Contact toEntity(ContactFormDTO dto);
}
