package com.example.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HttpStatus status;
        String message;

        if (authException instanceof AuthenticationCredentialsNotFoundException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Credentials not found. Please provide valid authentication.";
        } else if (authException instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid credentials. Please check your login details.";
        }else {
            status = HttpStatus.FORBIDDEN;
            message = "Access denied. You do not have permission to access this resource.";
        }

        response.setStatus(status.value());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getServletPath());
        body.put("timestamp", System.currentTimeMillis());

        mapper.writeValue(response.getOutputStream(), body);
    }
}