package org.example.services.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class BruteForceProtectorService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_MILLIS = 5 * 50 * 1000;

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public void failedAuthentication(String username) {
        if (attempts.containsKey(username)) {
            LoginAttempt attempt = attempts.get(username);
            attempt.count++;

            if (attempts.get(username).count >= MAX_ATTEMPTS) {
                attempt.lockTime = Instant.now().plusMillis(LOCK_TIME_MILLIS);
                attempt.count = 0;
            }
            return;
        }
        attempts.put(username, new LoginAttempt(1, null));
    }

    public boolean isBlocked(String username) {
        if (attempts.containsKey(username)) {
            LoginAttempt attempt = attempts.get(username);

            if (attempt.lockTime != null) {
                return attempt.lockTime.isAfter(Instant.now());
            }
        }
        return false;
    }

    public Optional<Instant> getLockTime(String username) {
        if (attempts.containsKey(username)) {
            LoginAttempt attempt = attempts.get(username);
            return Optional.of(attempt.lockTime);
        }
        return Optional.empty();
    }

    private static class LoginAttempt {
        private int count;
        private Instant lockTime;

        public LoginAttempt(int count, Instant lockTime) {
            this.count = count;
            this.lockTime = lockTime;
        }
    }
}
