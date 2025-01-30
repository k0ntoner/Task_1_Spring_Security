package org.example.models.trainer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeViewDto;
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
public class TrainerViewDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    private boolean isActive;

    private TrainingType specialization;
    @NotNull
    @Builder.Default
    private Collection<TrainerViewDto.TraineeViewDto> trainees = new ArrayList<>();

    @NotNull
    @Builder.Default
    private Collection<TrainingListViewDto> trainings = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TraineeViewDto {
        @NotNull
        private String username;

        @NotNull
        private String firstName;

        @NotNull
        private String lastName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;

        private String address;
    }
}
