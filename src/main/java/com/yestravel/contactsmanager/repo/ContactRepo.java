package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepo extends JpaRepository<Contact, Long> {

    @Override
    Page<Contact> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM contacts c WHERE " +
            "DAYOFYEAR(c.birth_date) >= DAYOFYEAR(CURDATE() + INTERVAL 1 DAY) " +
            "ORDER BY MONTH(c.birth_date) ASC, DAY(c.birth_date) ASC, c.id ASC",
            countQuery = "SELECT count(*) FROM contacts WHERE DAYOFYEAR(birth_date) >= DAYOFYEAR(CURDATE() + INTERVAL 1 DAY)",
            nativeQuery = true)
    Page<Contact> findUpcomingBirthdays(Pageable pageable);
}
