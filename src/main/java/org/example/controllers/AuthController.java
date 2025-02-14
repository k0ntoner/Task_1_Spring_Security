package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.models.user.AuthUserDto;
import org.example.repositories.entities.User;
import org.example.services.UserService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserDto user) {
        if (userService.matchPassword(user.getUsername(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }


}
