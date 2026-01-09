package com.covid.dashboard.service;

import com.covid.dashboard.model.User;
import com.covid.dashboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    private OAuth2User oauth2User;

    @BeforeEach
    void setUp() {
        oauth2User = mock(OAuth2User.class);
    }

    @Test
    void processOAuth2User_ExistingUser_UpdatesName() {
        String email = "test@example.com";
        String newName = "New Name";
        
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(oauth2User.getAttribute("name")).thenReturn(newName);
        
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setName("Old Name");
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        
        OAuth2User result = customOAuth2UserService.processOAuth2User(oauth2User);
        
        assertEquals(oauth2User, result);
        assertEquals(newName, existingUser.getName());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void processOAuth2User_NewUser_Registers() {
        String email = "new@example.com";
        String name = "New User";
        
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(oauth2User.getAttribute("name")).thenReturn(name);
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        OAuth2User result = customOAuth2UserService.processOAuth2User(oauth2User);
        
        assertEquals(oauth2User, result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void processOAuth2User_NoEmail_ThrowsException() {
        when(oauth2User.getAttribute("email")).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            customOAuth2UserService.processOAuth2User(oauth2User);
        });
        
        verify(userRepository, never()).save(any(User.class));
    }
}
