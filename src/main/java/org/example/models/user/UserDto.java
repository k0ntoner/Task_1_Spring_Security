package org.example.models.user;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class UserDto {
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String username;

    @NotNull
    @Size(min = 4, max = 10, message = "Password must be between 4 and 10 characters")
    private String password;

    @NotNull
    @Builder.Default
    private boolean isActive = true;
}
