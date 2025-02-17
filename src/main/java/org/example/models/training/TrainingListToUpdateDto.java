package org.example.models.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingListToUpdateDto {
    @NotNull
    @Builder.Default
    private Collection<TrainingUpdateDto> trainings= new ArrayList<>();


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrainingUpdateDto {
        private Long id;

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
    }
}
