package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.model.Contact;
import com.yestravel.contactsmanager.service.IContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    IContactService contactService;

    @GetMapping("/")
    public String listContact(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Page<Contact> contactPage = contactService.getContacts(page, size);
        contactPage.forEach(contact -> logger.info(contact.toString()));
        model.addAttribute("contactPage",contactPage);
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

    @GetMapping("/birthday")
    public String showBirthdays(ModelMap model) {
        List<Contact> birthdayContactList = contactService.birthdayListContact();
        birthdayContactList.forEach(contact -> logger.info(contact.toString()));
        model.put("birthdayContactList", birthdayContactList);
        return "birthday"; //birthday.html
    }
}
