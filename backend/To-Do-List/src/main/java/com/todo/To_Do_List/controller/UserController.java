package com.todo.To_Do_List.controller;

import com.todo.To_Do_List.model.User;
import com.todo.To_Do_List.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserController {

    private final UserService service;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService service, PasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }


    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> userRegister(@RequestBody User u) {
        try {
            service.createUser(u);
            return ResponseEntity.ok("Registrazione riuscita!");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Errore durante la registrazione: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> login(@RequestBody User loginRequest, HttpSession session) {
        User user = service.findByUsername(loginRequest.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato.");
        }

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password errata.");
        }


        GrantedAuthority roleUser = new SimpleGrantedAuthority("ROLE_USER");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(roleUser)
        );


        SecurityContextHolder.getContext().setAuthentication(auth);


        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return ResponseEntity.ok("Login riuscito!");
    }


    @GetMapping("/user")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> getUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non autenticato.");
        }
        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("/logout")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout effettuato.");
    }
}
