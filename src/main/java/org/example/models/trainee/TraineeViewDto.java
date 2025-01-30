package org.example.models.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;
import org.example.models.trainer.TrainerListViewDto;
import org.example.models.training.TrainingListViewDto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeViewDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    private boolean isActive;

    @NotNull
    @Builder.Default
    private Collection<TrainerListViewDto> trainers = new ArrayList<>();

    @NotNull
    @Builder.Default
    private Collection<TrainingListViewDto> trainings = new ArrayList<>();


}
