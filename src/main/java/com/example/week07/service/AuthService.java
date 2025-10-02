package com.example.week07.service;

import com.example.week07.domain.AppUser;
import com.example.week07.dto.Dtos.LoginDTO;
import com.example.week07.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public String login(LoginDTO dto) {
        if (dto.email == null || dto.password == null) throw new IllegalArgumentException("Email and password required");
        AppUser user = userService.findByEmail(dto.email).orElseThrow(() -> new IllegalArgumentException("Unknown email"));
        if (!userService.passwordEncoder().matches(dto.password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return jwtService.generateToken(user.getId(), user.getEmail());
    }
}
