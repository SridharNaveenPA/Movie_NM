package com.example.moviereview.controller;

import com.example.moviereview.dto.AuthDtos;
import com.example.moviereview.model.User;
import com.example.moviereview.security.JwtServicess;
import com.example.moviereview.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtServicess jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtServicess jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {
        if (userService.existsByUsername(req.username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userService.existsByEmail(req.email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User u = userService.register(req.username, req.email, req.password);
        String token = jwtService.generateToken(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, u.getUsername(), u.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username, req.password)
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
        User u = userService.findByUsername(req.username).orElseThrow();
        String token = jwtService.generateToken(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, u.getUsername(), u.getRole()));
    }
}


