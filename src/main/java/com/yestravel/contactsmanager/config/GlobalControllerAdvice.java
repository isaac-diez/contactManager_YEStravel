package com.yestravel.contactsmanager.config;

import com.yestravel.contactsmanager.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("username")
    public String globalUsername(@AuthenticationPrincipal CustomUserDetails user) {
        return user != null ? user.getUsername() : null;
    }
}
