package com.example.controller;

import com.example.commons.LoginRequest;
import com.example.commons.UserResponseDTO;
import com.example.commons.RegisterRequest;
import com.example.models.UserPrincipal;
import com.example.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<?>  login(@RequestBody LoginRequest loginRequest){
        System.out.println("Endpoint hit" + loginRequest);
        //return new ResponseEntity<UserResponseDTO>(authService.getUserByUsername(loginRequest.credential()),HttpStatus.OK);
        return new ResponseEntity<String>(authService.login(loginRequest),HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        System.out.println("endpoint register hit with: " + registerRequest);
        return new ResponseEntity<UserResponseDTO>(authService.register(registerRequest),HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public String me(HttpServletRequest request) {

        System.out.println("ME HIT");

        System.out.println("X-User-Id: " + request.getHeader("X-User-Id"));
        System.out.println("X-Username: " + request.getHeader("X-Username"));
        System.out.println("X-Role: " + request.getHeader("X-Role"));

        return "OK";
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(Authentication auth) {
        System.out.println(auth);
        return "Only admin can access";
    }
}
