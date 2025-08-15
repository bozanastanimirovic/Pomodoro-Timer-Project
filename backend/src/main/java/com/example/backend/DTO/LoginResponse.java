package com.example.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
	
	private String username;
    private List<String> roles;
    private String jwtToken;
    private String refreshToken;
}