package org.example.models.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import org.example.enums.TrainingType;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainee.TraineeDto;

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
    private TraineeDto traineeDto;

    @NotNull
    private TrainerDto trainerDto;

    @NotNull
    private String trainingName;

    @NotNull
    private TrainingType trainingType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime trainingDate;

    @NotNull
    private Duration trainingDuration;
}

