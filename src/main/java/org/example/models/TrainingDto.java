package org.example.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.example.repositories.entities.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class TrainingDto {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime trainingDate;
    private Duration trainingDuration;
}

