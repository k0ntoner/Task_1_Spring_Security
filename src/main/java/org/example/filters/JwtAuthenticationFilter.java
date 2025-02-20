package org.example.filters;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.services.security.BruteForceProtectorService;
import org.example.services.security.JwtTokenService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/", "/trainees/registration", "/trainers/registration"
    );

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BruteForceProtectorService bruteForceProtectorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (EXCLUDED_PATHS.stream().anyMatch(string -> request.getRequestURI().startsWith(string))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (jwtTokenService.isBlocked(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token is blocked");
            throw new JwtException("Token is blocked");
        }

        String username = JwtUtil.extractUsername(token);
        if (bruteForceProtectorService.isBlocked(username)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User is blocked until " + bruteForceProtectorService.getLockTime(username));
            throw new LockedException("User is blocked until " + bruteForceProtectorService.getLockTime(username));
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (JwtUtil.validateToken(token, userDetails)) {
                if(JwtUtil.isTokenExpired(token)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Token is Expired");
                    throw new JwtException("Token is Expired");
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
