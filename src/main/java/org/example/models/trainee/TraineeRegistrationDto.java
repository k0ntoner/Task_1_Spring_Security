package org.example.models.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRegistrationDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


    @NotNull
    @Size(min = 4, max = 10, message = "Password must be between 4 and 10 characters")
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;
}
