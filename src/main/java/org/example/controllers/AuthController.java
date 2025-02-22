package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.models.user.AuthUserDto;
import org.example.services.AuthService;
import org.example.services.security.BruteForceProtectorService;
import org.example.services.security.JwtTokenService;
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
    private AuthService authService;

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
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(user));
    }

}
