package com.example.models;

import com.example.commons.Role;

public record UserResponseDTO(
        Long id,
        String username,
        String password,
        Role role
) {}