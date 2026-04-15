package com.yestravel.contactsmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", ""})
    public String rootRedirect() {
        return "redirect:/contacts";
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {return ResponseEntity.ok("OK");
    }
}
