package com.example.service;

import com.example.commons.LoginRequest;
import com.example.commons.Role;
import com.example.commons.UserResponseDTO;
import com.example.entity.User;
import com.example.commons.RegisterRequest;
import com.example.commons.UserDTO;
import com.example.models.UserMapper;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public  UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDTO getUserById(Long id){
        User user =  userRepository.findById(id).orElseThrow(()-> new EntityExistsException("User with the given ID doesn't exist"));
        return UserMapper.maptoDTO(user);
    }

    public UserResponseDTO getUserByUsername(String username){
        User user =  userRepository.findByUsername(username).orElseThrow(()-> new EntityExistsException("User with the given ID doesn't exist"));
        return UserMapper.maptoDTO(user);
    }

    public UserResponseDTO registerUser(RegisterRequest request){
        if (userRepository.findByUsername(request.username()).isPresent())
            throw new EntityExistsException("The username was already taken");
        if (userRepository.findByEmail(request.email()).isPresent())
            throw new EntityExistsException("The email was already taken");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User(request.username(),request.email(),passwordEncoder.encode(request.password()), Role.REGISTERED);
        userRepository.save(user);
        return  UserMapper.maptoDTO(user);
    }

    public UserResponseDTO attemptLogin(LoginRequest loginRequest) {

        String credential = loginRequest.credential();
        String rawPassword = loginRequest.password();

        return userRepository.findByUsername(credential)
                .or(() -> userRepository.findByEmail(credential))
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .map(UserMapper::maptoDTO)
                .orElse(null);
    }


}





