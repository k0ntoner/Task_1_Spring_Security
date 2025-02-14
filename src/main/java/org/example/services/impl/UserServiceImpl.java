package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.models.user.AuthUserDto;
import org.example.repositories.UserDao;
import org.example.repositories.entities.User;
import org.example.services.UserService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService<AuthUserDto> {

    @Autowired
    @Qualifier("userDaoImpl")
    private UserDao<User> userDao;

    @Autowired
    private ConversionService conversionService;

    @Override
    public Optional<AuthUserDto> findByUsername(String username) {
        log.info("Request to find user with username: {}", username);
        Optional<User> user = userDao.findByUsername(username);
        if (user.isPresent()) {
            AuthUserDto loginUserDto = conversionService.convert(user.get(), AuthUserDto.class);
            return Optional.of(loginUserDto);
        }
        return Optional.empty();
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
}
