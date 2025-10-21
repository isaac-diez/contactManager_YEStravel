package com.yestravel.contactsmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", ""})
    public String rootRedirect() {
        return "redirect:/contacts";
    }
}
