package org.example.models.training;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingListViewDto {
    @NotNull
    private String trainingName;

    @NotNull
    private TrainingType trainingType;

    @NotNull
    private LocalDateTime trainingDateTime;

    @NotNull
    private Duration trainingDuration;

    @NotNull
    private String traineeUsername;

    @NotNull
    private String trainerUsername;
}
