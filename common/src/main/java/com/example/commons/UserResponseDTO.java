package com.example.commons;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        Role role
) {}