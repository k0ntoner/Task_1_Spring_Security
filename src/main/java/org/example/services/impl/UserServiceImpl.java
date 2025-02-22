package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.models.user.AuthUserDto;
import org.example.repositories.UserDao;
import org.example.repositories.entities.User;
import org.example.services.AuthService;
import org.example.services.UserService;
import org.example.services.security.BruteForceProtectorService;
import org.example.services.security.JwtTokenService;
import org.example.utils.JwtUtil;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements AuthService {

    @Autowired
    @Qualifier("userDaoImpl")
    private UserDao<User> userDao;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private BruteForceProtectorService bruteForceProtectorService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public AuthUserDto findByUsername(String username) {
        log.info("Request to find user with username: {}", username);
        Optional<User> user = userDao.findByUsername(username);
        if (user.isPresent()) {
            return conversionService.convert(user.get(), AuthUserDto.class);
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Request to change password for trainee with username: {}", username);
        Optional<User> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (UserUtils.passwordMatch(oldPassword, user.getPassword())) {
                user.setPassword(UserUtils.hashPassword(newPassword));
                userDao.update(user);
                return;
            }

            throw new IllegalArgumentException("Invalid password");
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public void activate(AuthUserDto authUserDto) {
        log.info("Request to activate user with username: {}", authUserDto.getUsername());
        Optional<User> optionalUser = userDao.findByUsername(authUserDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(true);
            userDao.update(user);
        }
    }

    @Override
    public void deactivate(AuthUserDto authUserDto) {
        log.info("Request to activate user with username: {}", authUserDto.getUsername());
        Optional<User> optionalUser = userDao.findByUsername(authUserDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            userDao.update(user);
        }
    }

    @Override
    public boolean matchPassword(String username, String password) {
        log.info("Request to match password");
        Optional<User> trainee = userDao.findByUsername(username);
        if (trainee.isPresent()) {
            return UserUtils.passwordMatch(password, trainee.get().getPassword());
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public String login(AuthUserDto user) {
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            if (!bruteForceProtectorService.isBlocked(user.getUsername())) {
                if (matchPassword(user.getUsername(), user.getPassword())) {
                    String token = JwtUtil.generateToken(user.getUsername());
                    activate(user);
                    jwtTokenService.addToken(token);
                    return token;
                }
                bruteForceProtectorService.failedAuthentication(user.getUsername());

                throw new IllegalArgumentException("Invalid password");
            }
            throw new LockedException("Username is blocked until " + bruteForceProtectorService.getLockTime(user.getUsername()));
        }
        throw new IllegalArgumentException("Invalid username");
    }
}
