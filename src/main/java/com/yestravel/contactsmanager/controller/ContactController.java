package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.service.IContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    IContactService contactService;

    @GetMapping("/")
    public String init(ModelMap model) {
        List<Contact> contacts = contactService.listContact();
        contacts.forEach(contact -> logger.info(contact.toString()));
        model.put("contacts",contacts);
        return "index"; //index.html
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

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable(value="id") int idContact, ModelMap model){
        Contact contact = contactService.findContactById(idContact);
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
    public String showDelete(@PathVariable(value="id") int idContact, ModelMap model){
        Contact contact = contactService.findContactById(idContact);
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
}
