package org.example.services.security;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JwtTokenService {
    private final List<String> blackTokensList = new ArrayList<>();

    private final List<String> whiteTokensList = new ArrayList<>();

    public void addToken(String token) {
        if (!blackTokensList.contains(token)) {
            if(!whiteTokensList.contains(token)) {
                whiteTokensList.add(token);
                return;
            }
            throw new JwtException("Token already exists");
        }
        throw new JwtException("Token already blocked");
    }
    public void blockToken(String token) {
        whiteTokensList.remove(token);
        blackTokensList.add(token);
    }

    public boolean isBlocked(String token) {
        return !whiteTokensList.contains(token);
    }

}
