package org.example.utils;

import org.apache.commons.text.RandomStringGenerator;

public class PasswordGenerator {
    static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('!', 'z')
            .filteredBy(Character::isLetterOrDigit, c -> "!@#$%^&*()".indexOf(c) >= 0)
            .build();
    public static String generatePassword() {
        return generator.generate(10);

    }
}
