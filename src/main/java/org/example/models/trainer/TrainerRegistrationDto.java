package org.example.models.trainer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


    @NotNull
    @Size(min = 4, max = 10, message = "Password must be between 4 and 10 characters")
    private String password;

    @NotNull
    private TrainingType specialization;
}
