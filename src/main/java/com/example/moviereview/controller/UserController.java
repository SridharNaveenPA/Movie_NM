package com.example.moviereview.controller;

import com.example.moviereview.model.User;
import com.example.moviereview.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal(expression = "username") String username) {
        if (username == null) return ResponseEntity.status(401).build();
        User user = userService.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(Map.<String, Object>of(
                "user_id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));
    }
}


