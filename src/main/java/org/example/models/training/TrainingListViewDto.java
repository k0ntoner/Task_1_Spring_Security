package org.example.models.training;

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
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDateTime;
    private Duration trainingDuration;
    private String traineeUsername;
    private String trainerUsername;
}
