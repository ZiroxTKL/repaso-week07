package com.example.week07.service;

import com.example.week07.domain.AppUser;
import com.example.week07.dto.Dtos.RegisterUserDTO;
import com.example.week07.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) return false;
        // at least 1 uppercase A-Z
        return Pattern.compile("[A-Z]").matcher(name).find();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasLetter = Pattern.compile("[A-Za-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        return hasLetter && hasDigit;
    }

    @Transactional
    public UUID register(RegisterUserDTO dto) {
        if (!isValidName(dto.firstName) || !isValidName(dto.lastName) || !isValidEmail(dto.email) || !isValidPassword(dto.password)) {
            throw new IllegalArgumentException("Invalid user data");
        }
        userRepository.findByEmailIgnoreCase(dto.email).ifPresent(u -> { throw new IllegalArgumentException("Email already exists"); });
        AppUser user = new AppUser();
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setEmail(dto.email.toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(dto.password));
        user = userRepository.save(user);
        return user.getId();
    }

    public Optional<AppUser> findByEmail(String email) { return userRepository.findByEmailIgnoreCase(email); }
    public AppUser getById(UUID id) { return userRepository.findById(id).orElseThrow(); }
    public void deleteAll(){ userRepository.deleteAll(); }
    public PasswordEncoder passwordEncoder() { return passwordEncoder; }
}
