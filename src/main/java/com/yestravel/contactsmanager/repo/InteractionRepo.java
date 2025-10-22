package com.yestravel.contactsmanager.repo;

import com.yestravel.contactsmanager.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepo extends JpaRepository<Interaction, Long> {}
