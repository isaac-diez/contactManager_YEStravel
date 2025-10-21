package com.yestravel.contactsmanager.controller;

import com.yestravel.contactsmanager.dto.AuthRequest;
import com.yestravel.contactsmanager.dto.AuthResponse;
import com.yestravel.contactsmanager.dto.RegisterRequest;
import com.yestravel.contactsmanager.exception.UsernameAlreadyExistsException;
import com.yestravel.contactsmanager.model.User;
import com.yestravel.contactsmanager.security.JwtService;
import com.yestravel.contactsmanager.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtService.generateToken(userService.loadUserByUsername(request.getUsername()));

        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * 24)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        AuthResponse responseBody = new AuthResponse(token);

        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }
}



