package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.dto.InteractionRequest;
import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.service.ContactService;
import com.yestravel.contactsmanager.service.InteractionService;
import com.yestravel.contactsmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interactions")
public class InteractionController {

    private final InteractionService interactionService;
    private final ContactService contactService;
    private final UserService userService;

    public InteractionController(
            InteractionService interactionService,
            ContactService contactService,
            UserService userService) {
        this.interactionService = interactionService;
        this.contactService = contactService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Interaction> findAllInteractions() {
        return interactionService.findAllInteractions();
    }

    @GetMapping("/{id}")
    public Interaction findInteractionById(@PathVariable Long id) {
        return interactionService.findInteractionById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Interaction> addInteraction(@RequestBody InteractionRequest request) {
        Interaction created = interactionService.addInteraction(
                request.getContactId(),
                request.getUsername(),
                request.getType(),
                request.getDescription()
        );
        return ResponseEntity.ok(created);
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
