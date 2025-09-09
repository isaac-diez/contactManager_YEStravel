package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

    @Override
    Page<Contact> findAll(Pageable pageable);
}
