package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;

import org.example.repositories.entities.Training;
import org.example.repositories.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
@Slf4j
public class UserUtils {
    private static  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
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
    public static String generatePassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('!', 'z')
                .filteredBy(Character::isLetterOrDigit, c -> "!@#$%^&*()".indexOf(c) >= 0)
                .build();
        return generator.generate(10);

    }
    public static boolean verifyPassword(String password){
        if(password==null){
            log.error("Password must not be null");
            return false;
        }
        if(password.length() < MIN_LENGTH || password.length() > MAX_LENGTH){
            log.error("Password must have between 4 and 10 characters");
            return false;
        }
        return true;
    }
    public static boolean validateUser(User user){
        if(user==null){
            log.error("UserDto must not be null");
            return false;
        }
        if(user.getFirstName()==null){
            log.error("First name must not be null");
            return false;
        }
        if(user.getLastName()==null){
            log.error("Last name must not be null");
            return false;
        }
        if(user.getUsername()==null){
            log.error("Username must not be null");
            return false;
        }

        if(user.getPassword()==null){
            log.error("Password must not be null");
            return false;
        }
        if(user.getIsActive()==null){
            log.error("Is active must not be null");
            return false;
        }
        return true;
    }
    public static boolean validateTraining(Training training){
        if(training==null){
            log.error("Training must not be null");
            return false;
        }
        if(training.getTrainee()==null){
            log.error("Trainee must not be null");
            return false;
        }
        if(training.getTrainer()==null){
            log.error("Trainer must not be null");
            return false;
        }
        if(training.getTrainingDate()==null){
            log.error("Training date must not be null");
            return false;
        }
        if(training.getTrainingName()==null){
            log.error("TrainingName must not be null");
            return false;
        }
        if(training.getTrainingType()==null){
            log.error("TrainingType must not be null");
            return false;
        }
        if(training.getTrainingDuration()==null){
            log.error("TrainingDuration must not be null");
            return false;
        }
        return true;
    }
}
