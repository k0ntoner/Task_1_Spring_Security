package org.example.models.trainee;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import org.example.enums.TrainingType;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
public class TraineeUpdateDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    @Builder.Default
    private boolean isActive = true;

    private LocalDate dateOfBirth;

    private String address;
}
