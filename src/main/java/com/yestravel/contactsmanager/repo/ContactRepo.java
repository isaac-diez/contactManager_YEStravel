package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

    @Override
    Page<Contact> findAll(Pageable pageable);

    @Query("SELECT c FROM Contact c " +
            "WHERE FUNCTION('MONTH', c.birthDate) = :month")
    Page<Contact> findByBirthDateMonth(@Param("month") int month, Pageable pageable);
}
