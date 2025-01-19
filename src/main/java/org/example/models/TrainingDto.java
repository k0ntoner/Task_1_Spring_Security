package org.example.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import org.example.enums.TrainingType;
import org.example.repositories.entities.Trainee;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class TrainingDto {
    private Long id;

    @NotNull
    private TraineeDto trainee;

    @NotNull
    private TrainerDto trainer;

    @NotNull
    private String trainingName;

    @NotNull
    private TrainingType trainingType;

    @NotNull
    private LocalDateTime trainingDate;

    @NotNull
    private Duration trainingDuration;
}

