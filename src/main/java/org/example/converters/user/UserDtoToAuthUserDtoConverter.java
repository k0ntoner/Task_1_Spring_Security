package org.example.converters.user;

import org.example.models.user.AuthUserDto;
import org.example.models.user.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToAuthUserDtoConverter implements Converter<UserDto, AuthUserDto> {
    @Override
    public AuthUserDto convert(UserDto userDto) {
        return AuthUserDto.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }
}
