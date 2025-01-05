package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    @JsonProperty("isActive")
    private boolean isActive;
}
