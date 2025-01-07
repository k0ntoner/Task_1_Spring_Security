package org.example.utils;

import org.apache.commons.text.RandomStringGenerator;
import org.example.models.User;

import java.util.function.Function;

public class UserUtils {

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

    public static String generatePassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('!', 'z')
                .filteredBy(Character::isLetterOrDigit, c -> "!@#$%^&*()".indexOf(c) >= 0)
                .build();
        return generator.generate(10);

    }
}
