package com.example.models;

import com.example.commons.UserResponseDTO;
import com.example.entity.User;
import com.example.commons.UserDTO;
import java.util.List;

public class UserMapper {
    public UserMapper(){

    }

    public static UserResponseDTO maptoDTO(User user){
        if (user == null){
            throw new RuntimeException("user is null");
        }
        return new UserResponseDTO(user.getId(),user.getUsername(), user.getEmail(),user.getRole());
    }
    public static List<UserResponseDTO> mapListtoDTO(List<User> users){
        if (users == null) return List.of();
        return users.stream().map(UserMapper::maptoDTO).toList();
    }
}
