package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepo extends JpaRepository<Interaction, Long> {
    Page<Interaction> findAllInteractionsByContact_Id(Long contactId, Pageable pageable);

}
