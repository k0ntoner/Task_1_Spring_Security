package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.models.user.AuthUserDto;
import org.example.services.UserService;
import org.example.services.security.BruteForceProtectorService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "Authentication Controller", description = "Controller for user authentication")
public class AuthController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService<AuthUserDto> userService;

    @Autowired
    private BruteForceProtectorService bruteForceProtectorService;

    @PostMapping("/login")
    @Operation(summary = "Login into system",
            parameters = {
                    @Parameter(name = "AuthUserDto", description = "User's name and password")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid Credentials"),
    })
    public ResponseEntity<?> login(@RequestBody AuthUserDto user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            if (!bruteForceProtectorService.isBlocked(user.getUsername())) {
                if (userService.matchPassword(user.getUsername(), user.getPassword())) {
                    String token = JwtUtil.generateToken(user.getUsername());
                    userService.activate(user);
                    return ResponseEntity.ok(token);
                }
                bruteForceProtectorService.failedAuthentication(user.getUsername());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied until " + bruteForceProtectorService.getLockTime(user.getUsername()).get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Credentials");
    }

}
