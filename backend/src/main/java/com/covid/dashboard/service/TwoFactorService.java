package com.covid.dashboard.service;

import com.covid.dashboard.model.User;
import com.covid.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TwoFactorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(User user) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setTwoFactorCode(otp);
        user.setTwoFactorExpiry(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 mins
        userRepository.save(user);
        emailService.sendOtp(user.getEmail(), otp);
    }

    public boolean validateOtp(User user, String otp) {
        if (user.getTwoFactorCode() == null || user.getTwoFactorExpiry() == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(user.getTwoFactorExpiry())) {
            return false;
        }
        return user.getTwoFactorCode().equals(otp);
    }
}
