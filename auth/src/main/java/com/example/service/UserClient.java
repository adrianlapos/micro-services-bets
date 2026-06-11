package com.example.service;


import com.example.commons.LoginRequest;
import com.example.commons.RegisterRequest;
import com.example.commons.UserResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.cloud.openfeign.FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/api/users/username/{username}")
    UserResponseDTO getByUsername(@PathVariable("username") String username);
    @PostMapping("/api/users/login")
    UserResponseDTO attemptLogin(@RequestBody LoginRequest loginRequest);
    @PostMapping("/api/users/register")
    UserResponseDTO register(@RequestBody RegisterRequest registerRequest);
}
