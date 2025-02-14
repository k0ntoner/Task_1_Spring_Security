package org.example.converters.user;

import org.example.models.user.AuthUserDto;
import org.example.repositories.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToAuthUserDtoConverter implements Converter<User, AuthUserDto> {
    @Override
    public AuthUserDto convert(User user) {
        return AuthUserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword()).build();
    }
}
