package org.example.converters.user;

import org.example.models.user.LoginUserDto;
import org.example.models.user.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToLoginUserDtoConverter implements Converter<UserDto, LoginUserDto> {
    @Override
    public LoginUserDto convert(UserDto userDto) {
        return LoginUserDto.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
    }
}
