package org.example.models.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserPasswordDto {
    @NotNull
    private String username;

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
