package com.example.backend.services.impl;

public interface MailService {
    void sendPasswordResetEmail(String to, String resetUrl);

    void sendVerifyEmail(String email, String verifyUrl);
}
