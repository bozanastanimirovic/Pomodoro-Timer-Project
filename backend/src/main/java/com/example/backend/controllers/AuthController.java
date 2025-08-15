package com.example.backend.controllers;

import com.example.backend.DTO.LoginRequest;
import com.example.backend.DTO.LoginResponse;
import com.example.backend.DTO.RegisterRequest;
import com.example.backend.DTO.ResetPasswordDTO;
import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.models.enums.UserRole;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.security.services.UserDetailsServiceImpl;
import com.example.backend.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/public")
public class AuthController {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getUsername(),
                roles, jwtToken, refreshToken);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getName(),
                registerRequest.getSurname(),
                encoder.encode(registerRequest.getPassword()));

        Role role = roleRepository.findByRoleName(UserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {

        if (jwtUtils.validateRefreshToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String newJwtToken = jwtUtils.generateTokenFromUsername(userDetails);
            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("accessToken", "Bearer " + newJwtToken);
            responseMap.put("refreshToken", newRefreshToken);

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, HttpServletRequest request) {
        String sourceUrl = request.getRequestURL().toString();

        try{
            userService.generatePasswordResetToken(email, sourceUrl);
            return ResponseEntity.ok("Password reset token generated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error resetting email");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> register(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            userService.resetPassword(resetPasswordDTO.getToken(), resetPasswordDTO.getPassword());
            return ResponseEntity.ok("Password reset successfully!");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Error resetting password, " +e.getMessage());
        }
    }

    @PostMapping("/send-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email, HttpServletRequest request) {
        String sourceUrl = request.getRequestURL().toString();
        try{
            userService.sendVerificationEmail(email, sourceUrl);
            return ResponseEntity.ok("Verification email sent successfully!");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Error sending verification email");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        try{
            userService.verifyUser(token);
            return ResponseEntity.ok("User verified successfully!");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error verifying user");
        }
    }
}
