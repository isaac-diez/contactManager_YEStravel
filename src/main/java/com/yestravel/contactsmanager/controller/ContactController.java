package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.model.InteractionType;
import com.yestravel.contactsmanager.service.ContactService;
import com.yestravel.contactsmanager.service.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;
    private final InteractionService interactionService;

    public ContactController(ContactService contactService, InteractionService interactionService) {
        this.contactService = contactService;
        this.interactionService = interactionService;
    }

    @GetMapping({"/", ""})
    public String listContact(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<Contact> contactPage = contactService.getContacts(page, size);
        contactPage.forEach(contact -> logger.info(contact.toString()));
        model.addAttribute("contactPage",contactPage);
        return "contacts"; //contacts.html
    }

    @GetMapping("/add")
    public String showAdd(){
        return "add"; //add.html
    }

    @PostMapping("/add")
    public String addContact(@ModelAttribute("contactForm") Contact contactForm){
        logger.info("Contact to add: " + contactForm);
        contactService.saveContact(contactForm);
        return "redirect:/"; //redirect controller to path "/"
    }

    @GetMapping("/view/{id}")
    public String viewContact(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Contact contact = contactService.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        Page<Interaction> interactionPage = interactionService.findByContactId(id, page, 5);

        model.addAttribute("contact", contact);
        model.addAttribute("interactionPage", interactionPage);
        model.addAttribute("interactions", interactionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", interactionPage.getTotalPages());
        model.addAttribute("interactionTypes", InteractionType.values());

        logger.info("Contact to view: " + contact);

        return "viewContact"; //viewContact.html
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable(value="id") Long idContact, ModelMap model){
        Optional<Contact> contact = contactService.findById(idContact);
        logger.info("Contact to edit: " + contact);
        model.put("contactToEdit", contact);
        return "edit"; //edit.html
    }

    @PostMapping("/edit")
    public String editContact(@ModelAttribute("contactToEdit") Contact contact) {
        logger.info("Contact updated: " + contact);
        contactService.saveContact(contact);
        return "redirect:/"; //redirect controller to path "/"
    }

    @GetMapping("/delete/{id}")
    public String showDelete(@PathVariable(value="id") Long idContact, ModelMap model){
        Optional<Contact> contact = contactService.findById(idContact);
        logger.info("Contact to delete: " + contact);
        model.put("contactToDelete", contact);
        return "delete"; //delete.html
    }

    @PostMapping("/delete")
    public String deleteContact(@ModelAttribute("contactToDelete") Contact contact) {
        logger.info("Contact deleted: " + contact);
        contactService.deleteContact(contact);
        return "redirect:/"; //redirect controller to path "/"
    }

    @GetMapping("/birthday")
    public String listBirthdays(
        @RequestParam(name= "page", defaultValue = "0") int page,
        @RequestParam(name= "size", defaultValue = "10") int size,
        Model model) {

        Page<Contact> birthdayContactPage = contactService.getBirthdayContacts(page, size);
        birthdayContactPage.forEach(contact -> logger.info(contact.toString()));
        model.addAttribute("contactPage", birthdayContactPage);
        return "birthday"; //birthday.html

    }

}
