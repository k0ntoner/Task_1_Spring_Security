package org.example.models.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainee.TraineeDto;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingAddDto {
    @NotNull
    private String traineeUsername;

    @NotNull
    private String trainerUsername;

    @NotNull
    private String trainingName;

    @NotNull
    private TrainingType trainingType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime trainingDate;

    @NotNull
    private Duration trainingDuration;

    @JsonIgnore
    private TraineeDto traineeDto;

    @JsonIgnore
    private TrainerDto trainerDto;
}
