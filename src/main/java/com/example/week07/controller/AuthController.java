package com.example.week07.controller;

import com.example.week07.dto.Dtos.AuthToken;
import com.example.week07.dto.Dtos.LoginDTO;
import com.example.week07.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){ this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginDTO dto){
        String token = authService.login(dto);
        return ResponseEntity.ok(new AuthToken(token));
    }
}
