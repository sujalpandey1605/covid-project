package com.covid.dashboard.controller;

import com.covid.dashboard.model.User;
import com.covid.dashboard.repository.UserRepository;
import com.covid.dashboard.service.TwoFactorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 DISABLE SECURITY (OAuth, CSRF, JWT)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TwoFactorService twoFactorService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ SIGNUP SUCCESS
    @Test
    void signupSuccess() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode("1234"))
                .thenReturn("encoded-pass");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "email", "test@gmail.com",
                                        "password", "1234",
                                        "name", "Test User"
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    // ❌ SIGNUP FAIL (Email already exists)
    @Test
    void signupEmailExists() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "email", "test@gmail.com",
                                        "password", "1234",
                                        "name", "Test"
                                )
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    // ✅ LOGIN SUCCESS
    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encoded");

        Mockito.when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches("1234", "encoded"))
                .thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "email", "test@gmail.com",
                                        "password", "1234"
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP sent to email"));
    }

    // ❌ LOGIN FAIL
    @Test
    void loginInvalidCredentials() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "email", "test@gmail.com",
                                        "password", "wrong"
                                )
                        )))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    // ✅ OTP VERIFY SUCCESS
    @Test
    void verifyOtpSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");

        Mockito.when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        Mockito.when(twoFactorService.validateOtp(user, "123456"))
                .thenReturn(true);

        mockMvc.perform(post("/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "email", "test@gmail.com",
                                        "otp", "123456"
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    // ❌ OTP VERIFY FAIL
    @Test
    void verifyOtpFail() throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");

        Mockito.when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        Mockito.when(twoFactorService.validateOtp(user, "000000"))
                .thenReturn(false);

        mockMvc.perform(post("/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of(
                                        "email", "test@gmail.com",
                                        "otp", "000000"
                                )
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or expired OTP"));
    }
}
