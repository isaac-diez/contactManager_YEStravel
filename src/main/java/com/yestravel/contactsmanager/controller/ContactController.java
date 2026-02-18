package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.model.Interaction;
import com.yestravel.contactsmanager.model.InteractionType;
import com.yestravel.contactsmanager.service.ContactImportService;
import com.yestravel.contactsmanager.service.ContactService;
import com.yestravel.contactsmanager.service.InteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;
    private final InteractionService interactionService;
    private final ContactImportService contactImportService;

    public ContactController(ContactService contactService,
                             InteractionService interactionService,
                             ContactImportService contactImportService) {
        this.contactService = contactService;
        this.interactionService = interactionService;
        this.contactImportService = contactImportService;
    }

    @GetMapping({"/", ""})
    public String listContact(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<Contact> contactPage = contactService.getContacts(page, size);
        //contactPage.forEach(contact -> logger.info(contact.toString()));
        model.addAttribute("contactPage",contactPage);
        return "contacts"; //contacts.html
    }

    @GetMapping("/add")
    public String showAdd(Model model){
        model.addAttribute("contact", new Contact());
        return "add";
    }

    @PostMapping("/add")
    public String addContact(@ModelAttribute("contact") Contact contactForm, Principal principal){
        logger.info("Contact to add: " + contactForm);
        Contact savedContact = contactService.saveContact(contactForm, principal.getName());
        return "redirect:/contacts/view/" + savedContact.getId();
    }

    @GetMapping("/view/{id}")
    public String viewContact(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Contact contact = contactService.findById(id);

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
        Contact contact = contactService.findById(idContact);
        logger.info("Contact to edit: " + contact);
        model.put("contactToEdit", contact);
        return "edit"; //edit.html
    }

    @PostMapping("/edit")
    public String editContact(@ModelAttribute("contactToEdit") Contact contact, Principal principal) {
        logger.info("Contact updated: " + contact);
        contactService.saveContact(contact, principal.getName());
        return "edit"; //redirect controller to path "/"
    }

    @GetMapping("/delete/{id}")
    public String showDelete(@PathVariable(value="id") Long idContact, ModelMap model){
        Contact contact = contactService.findById(idContact);
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

    @PostMapping("/import")
    @ResponseBody
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please, select a file.");
        }

        try {
            contactImportService.importContacts(file);
            return ResponseEntity.ok("Import finished successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }

}
