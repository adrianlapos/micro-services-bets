package com.example.models;

import com.example.commons.Role;

public record UserPrincipal(Long id, String username, Role role) {
}
