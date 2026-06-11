package com.example.service;

import com.example.commons.RegisterRequest;
import com.example.commons.UserResponseDTO;
import com.example.commons.LoginRequest;
import com.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserClient userClient;
    private final JwtUtil jwtUtil;

    public AuthService(UserClient userClient,JwtUtil jwtUtil){
        this.userClient = userClient;
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDTO getUserByUsername(String username){
        UserResponseDTO userResponseDTO =  userClient.getByUsername(username);
        System.out.println("USER RECIEVED VIA FEIGN: " + userResponseDTO);
        return  userResponseDTO;
    }

    public String login(LoginRequest loginRequest){
        UserResponseDTO userResponseDTO = userClient.attemptLogin(loginRequest);
        if (userResponseDTO == null)
            throw new RuntimeException("User login unsuccesful");
        String token = jwtUtil.generateToken(userResponseDTO);
        System.out.println("Login succesful,token: "  + token);
        return token;
    }

    public UserResponseDTO register(RegisterRequest request){
        return userClient.register(request);
    }

}
