package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.repo.InteractionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionServiceImpl implements InteractionService{

    private final InteractionRepo interactionRepo;

    public InteractionServiceImpl(InteractionRepo interactionRepo) {
        this.interactionRepo = interactionRepo;
    }

    @Override
    public void saveInteraction(Interaction interaction) {
        interactionRepo.save(interaction);
    }

    @Override
    public void deleteInteraction(Interaction interaction) {
        interactionRepo.delete(interaction);
    }

    @Override
    public Interaction findInteractionById(Long id) {
        return interactionRepo.findById(id).orElse(null);
    }

    @Override
    public List<Interaction> findAllInteractions() {
        return interactionRepo.findAll();
    }

    @Override
    public List<Interaction> findAllInteractionsWithContactId(Integer contactId) {
        return List.of();
    }

    @Override
    public Interaction editInteraction(Interaction interaction) {
        return interactionRepo.save(interaction);
    }
}
