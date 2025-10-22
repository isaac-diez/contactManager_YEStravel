package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.service.InteractionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interactions")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @GetMapping("/all")
    public List<Interaction> findAllInteractions() {
        return interactionService.findAllInteractions();
    }

    @GetMapping("/contact/{contactId}")
    public List<Interaction> findAllInteractionsWithContactId(@PathVariable Integer contactId) {
        return interactionService.findAllInteractionsWithContactId(contactId);
    }

    @GetMapping("/{id}")
    public Interaction findInteractionById(@PathVariable Long id) {
        return interactionService.findInteractionById(id);
    }

    @PostMapping("/add")
    public void saveInteraction(@RequestBody Interaction interaction) {
        interactionService.saveInteraction(interaction);
    }

    @DeleteMapping("/delete")
    public void deleteInteraction(@RequestBody Interaction interaction) {
        interactionService.deleteInteraction(interaction);
    }

    @PostMapping("/edit")
    public Interaction editInteraction(@RequestBody Interaction interaction) {
        return interactionService.editInteraction(interaction);
    }
}
