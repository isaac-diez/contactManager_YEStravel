package com.yestravel.contactsmanager.service;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.model.InteractionType;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.repo.ContactRepo;
import com.yestravel.contactsmanager.repo.InteractionRepo;
import com.yestravel.contactsmanager.repo.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InteractionServiceImpl implements InteractionService{

    private final InteractionRepo interactionRepo;
    private final ContactRepo contactRepo;
    private final UserRepo userRepo;
    private final EntityManager entityManager;

    public InteractionServiceImpl(
            EntityManager entityManager,
            InteractionRepo interactionRepo,
            ContactRepo contactRepo,
            UserRepo userRepo) {
        this.entityManager= entityManager;
        this.interactionRepo = interactionRepo;
        this.contactRepo = contactRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Interaction createInteraction(Interaction interaction) {

        if (interaction.getContact() == null || interaction.getContact().getId() == null) {
            throw new IllegalArgumentException("Contact ID must be provided when creating an interaction");
        }
        if (interaction.getUser() == null || interaction.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID must be provided when creating an interaction");
        }

        Contact contactRef = entityManager.getReference(Contact.class, interaction.getContact().getId());
        User userRef = entityManager.getReference(User.class, interaction.getUser().getId());

        interaction.setContact(contactRef);
        interaction.setUser(userRef);
        return interactionRepo.save(interaction);
    }

    @Override
    public Interaction addInteraction(Long contactId, String username, InteractionType type, String description) {
        Contact contact = contactRepo.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User: " + username + " not found"));

        Interaction interaction = new Interaction();
        interaction.setContact(contact);
        interaction.setUser(user);
        interaction.setType(type);
        interaction.setDescription(description);
        interaction.setDate(LocalDateTime.now());

        return interactionRepo.save(interaction);
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
    public Page<Interaction> findByContactId(Long contactId, int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return interactionRepo.findAllInteractionsByContact_Id(contactId, pageRequest);
    }

    @Override
    public Interaction editInteraction(Interaction interaction) {
        return interactionRepo.save(interaction);
    }
}
