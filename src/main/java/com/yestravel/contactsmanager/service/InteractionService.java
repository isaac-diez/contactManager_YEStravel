package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Interaction;

import java.util.List;

public interface InteractionService {
    public void saveInteraction(Interaction interaction);

    public void deleteInteraction(Interaction interaction);

    public Interaction findInteractionById(Long id);

    public List<Interaction> findAllInteractions();

    public List<Interaction> findAllInteractionsWithContactId(Integer contactId);

    public Interaction editInteraction (Interaction interaction);
}
