package org.example.models.trainer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String username;

    @NotNull
    @Builder.Default
    private boolean isActive = true;

    @NotNull
    private TrainingType specialization;
}
