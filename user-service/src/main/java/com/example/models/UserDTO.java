package com.example.models;

import com.example.commons.Role;

public record UserDTO(Long id,String username, String email, Role role) {
}
