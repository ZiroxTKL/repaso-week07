package com.example.week07.controller;

import com.example.week07.dto.Dtos.NewIdDTO;
import com.example.week07.dto.Dtos.RegisterUserDTO;
import com.example.week07.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@RequestBody RegisterUserDTO dto) {
        UUID id = userService.register(dto);
        return ResponseEntity.ok(new NewIdDTO(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") UUID id) {
        var u = userService.getById(id);
        return ResponseEntity.ok(new Object(){
            public final UUID id = u.getId();
            public final String firstName = u.getFirstName();
            public final String lastName = u.getLastName();
            public final String email = u.getEmail();
        });
    }
}
