package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import org.example.repositories.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class UserUtils {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 10;

    public static String generateUserName(User entity, Function<String, Boolean> isUserNameExist) {
        String userName = entity.getFirstName() + "." + entity.getLastName();
        String uniqueUserName = userName;
        long counter = 1;

        while (isUserNameExist.apply(uniqueUserName)) {
            uniqueUserName = userName + counter;
            counter++;
        }

        return uniqueUserName;
    }

    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean passwordMatch(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }

    public static boolean verifyPassword(String password) {
        if (password != null) {
            if (password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH) {
                return true;
            }
        }
        log.error("Invalid password");
        return false;
    }

}
