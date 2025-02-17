package org.example.services.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.models.user.AuthUserDto;
import org.example.services.UserService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogoutService {
    @Autowired
    private UserService<AuthUserDto> userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenService jwtTokenService;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request to logout:");

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = JwtUtil.extractUsername(token);
            if (JwtUtil.validateToken(token, userDetailsService.loadUserByUsername(username))) {
                userService.deactivate(userService.findByUsername(username));
                jwtTokenService.blockToken(token);
                return;
            }
            throw new JwtException("Invalid JWT token");
        }
    }
}
