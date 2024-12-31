package org.example.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
