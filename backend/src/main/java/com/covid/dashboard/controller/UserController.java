package com.covid.dashboard.controller;

import com.covid.dashboard.model.User;
import com.covid.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            cleanUser(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String email;
        if (authentication.getPrincipal() instanceof OAuth2User) {
            email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        } else {
            email = authentication.getName();
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            cleanUser(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(404).body("User not found in database");
    }

    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        String email;
        if (authentication.getPrincipal() instanceof OAuth2User) {
            email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        } else {
            email = authentication.getName();
        }

        return userRepository.findByEmail(email)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok("Account deleted successfully");
                })
                .orElse(ResponseEntity.status(404).body("User not found"));
    }

    private void cleanUser(User user) {
        user.setPassword(null);
        user.setTwoFactorCode(null);
        user.setTwoFactorExpiry(null);
    }
}
