package com.example.academic_management_api.controller;

import com.example.academic_management_api.entity.Users;
import com.example.academic_management_api.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    public final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        List<Users> allUsers = userRepository.findAll();
        return allUsers;
    }
}
