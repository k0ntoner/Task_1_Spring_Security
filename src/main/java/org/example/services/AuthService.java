package org.example.services;

import org.example.models.user.AuthUserDto;

public interface AuthService extends UserService<AuthUserDto> {
    String login(AuthUserDto authUserDto);
}
