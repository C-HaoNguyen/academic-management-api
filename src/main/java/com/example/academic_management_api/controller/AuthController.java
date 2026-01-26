package com.example.academic_management_api.controller;

import com.example.academic_management_api.JwtTokenUtil;
import com.example.academic_management_api.dto.auth.AuthResponse;
import com.example.academic_management_api.dto.auth.LoginRequest;
import com.example.academic_management_api.dto.auth.SignupRequest;
import com.example.academic_management_api.entity.Users;
import com.example.academic_management_api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtTokenUtil jwtTokenUtil;

    public AuthController (UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.getSignupUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(request.getSignupEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Users user = new Users();
        user.setUsername(request.getSignupUsername());
        user.setFullName(request.getSignupFullName());
        user.setEmail(request.getSignupEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getSignupPassword()));
        user.setRole(request.getSignupRole());
        user.setActive(true);

        userRepository.save(user);

        return ResponseEntity.ok("Signup successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getActive()) {
            return ResponseEntity.badRequest().body("User is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Username or password is incorrect");
        }

        String accessToken = jwtTokenUtil.generateAccessToken(
                user.getUsername(),
                user.getRole()
        );

        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUsername());

        AuthResponse response = new AuthResponse(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                accessToken,
                refreshToken
        );

        return ResponseEntity.ok(response);
    }
}
