package com.covid.dashboard.service;

import com.covid.dashboard.model.User;
import com.covid.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        try {
            return processOAuth2User(oauth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    protected OAuth2User processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        System.out.println("Processing OAuth2 User: " + email);

        if (email == null) {
            System.err.println("Email not found from OAuth2 provider");
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            User user;
            if (userOptional.isPresent()) {
                user = userOptional.get();
                System.out.println("User found: " + email + ". Updating details.");
                if (name != null && !name.equals(user.getName())) {
                    user.setName(name);
                    userRepository.save(user);
                }
            } else {
                System.out.println("New Google user. Registering: " + email);
                user = new User();
                user.setEmail(email);
                user.setName(name);
                // Set a default scrambled password to avoid NOT NULL constraints in DB
                user.setPassword("OAUTH2_USER_" + java.util.UUID.randomUUID());
                userRepository.save(user);
            }
        } catch (Exception e) {
            System.err.println("Error saving/updating OAuth2 user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return oauth2User;
    }
}
