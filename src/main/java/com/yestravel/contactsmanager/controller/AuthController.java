package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.dto.AuthRequest;
import com.yestravel.contactsmanager.dto.AuthResponse;
import com.yestravel.contactsmanager.dto.RegisterRequest;
import com.yestravel.contactsmanager.exception.UsernameAlreadyExistsException;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.security.JwtService;
import com.yestravel.contactsmanager.service.UserService;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          UserService userService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) throws UsernameAlreadyExistsException {

        return userService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole());

    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtService.generateToken(userService.loadUserByUsername(request.getUsername()));
        return new AuthResponse(token);
    }
}



