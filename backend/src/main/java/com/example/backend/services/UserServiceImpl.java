package com.example.backend.services;

import com.example.backend.models.EmailToken;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.models.enums.UserRole;
import com.example.backend.repositories.EmailTokenRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.impl.MailService;
import com.example.backend.services.impl.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailTokenRepository emailTokenRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void incrementSessionCounter(String username) {
        User user = getUser(username);
        user.setSessionCounter(user.getSessionCounter()+1);
        userRepository.save(user);
    }

    @Override
    public User getById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: User not found."));
    }

    @Override
    public User create(User t) {
        return userRepository.save(t);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByName(String name){
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public User updateRole(UserRole userRole, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Role role = roleRepository.findByRoleName(userRole).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public void generatePasswordResetToken(String email, String sourceUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        EmailToken emailToken = new EmailToken(token, expiryDate,user);
        emailTokenRepository.save(emailToken);

        // TODO: change the frontUrl to specific new password page
        String frontUrl = sourceUrl.contains("5173") ? "http://localhost:5173" : "http://localhost:4200";
        String resetUrl = frontUrl + "?token=" + token;

        mailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        EmailToken resetToken = emailTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Token is expired");
        }
        if (resetToken.isUsed()){
            throw new RuntimeException("Token is used");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetToken.setUsed(true);
        emailTokenRepository.save(resetToken);
    }

    @Override
    public void sendVerificationEmail(String email, String sourceUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        EmailToken emailToken = new EmailToken(token, expiryDate,user);
        emailTokenRepository.save(emailToken);

        // TODO: change the frontUrl to specific new password page
        String frontUrl = sourceUrl.contains("5173") ? "http://localhost:5173" : "http://localhost:4200";
        String verifyUrl = frontUrl +"?token=" + token;

        mailService.sendVerifyEmail(user.getEmail(), verifyUrl);
    }

    @Override
    public void verifyUser(String token) {
        EmailToken verifyToken = emailTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
        if (verifyToken.isUsed()){
            throw new RuntimeException("Token is used");
        }
        if (verifyToken.getExpiryDate().isBefore(Instant.now())){
            throw new RuntimeException("Token is expired");
        }

        User user = verifyToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verifyToken.setUsed(true);
        emailTokenRepository.save(verifyToken);
    }
}
