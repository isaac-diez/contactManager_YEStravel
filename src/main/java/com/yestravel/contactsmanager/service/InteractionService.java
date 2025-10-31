package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.model.InteractionType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InteractionService {
    public Interaction createInteraction(Interaction interaction);

    public Interaction addInteraction(Long contactId, String username, InteractionType type, String description);

    public void deleteInteraction(Interaction interaction);

    public Interaction findInteractionById(Long id);

    public List<Interaction> findAllInteractions();

    public Page<Interaction> findByContactId(Long contactId, int page, int size);

    public Interaction editInteraction (Interaction interaction);
}
