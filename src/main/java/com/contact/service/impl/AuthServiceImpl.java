package com.contact.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contact.dto.request.RegisterRequest;
import com.contact.dto.response.RegisterResponse;
import com.contact.entity.AppUser;
import com.contact.exception.DuplicateContactException;
import com.contact.repository.AppUserRepository;
import com.contact.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (appUserRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateContactException(
                    "An account with this email already exists");
        }

        if (appUserRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new IllegalArgumentException(
                    "This username is already taken");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        AppUser savedUser = appUserRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt());
    }
}