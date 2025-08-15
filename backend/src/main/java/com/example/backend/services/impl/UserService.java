package com.example.backend.services.impl;

import com.example.backend.models.User;
import com.example.backend.models.enums.UserRole;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUser(String username);

    void incrementSessionCounter(String username);

    User getById(Long id);

    User create(User t);

    boolean existsById(Long id);

    void delete(Long id);

    List<User> getUsersByName(String name);

    User updateRole(UserRole userRole, Long id);

    void generatePasswordResetToken(String email, String sourceUrl);

    void resetPassword(String token, String newPassword);

    void sendVerificationEmail(String email, String sourceUrl);

    void verifyUser(String token);
}
