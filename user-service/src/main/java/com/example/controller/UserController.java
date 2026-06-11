package com.example.controller;

import com.example.commons.LoginRequest;
import com.example.entity.User;
import com.example.commons.RegisterRequest;
import com.example.models.UserDTO;
import com.example.commons.UserResponseDTO;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){

        return new ResponseEntity<UserResponseDTO>(userService.getUserById(id),HttpStatus.OK);
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("username") String username){
        return new ResponseEntity<UserResponseDTO>(userService.getUserByUsername(username),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        return new ResponseEntity<String>("User deleted",HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterRequest registerRequest){
        System.out.println("ENDPOINT HIT with: " + registerRequest);
        return new ResponseEntity<UserResponseDTO>(userService.registerUser(registerRequest),HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> attemptLogin(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userService.attemptLogin(loginRequest),HttpStatus.OK);
    }
}
